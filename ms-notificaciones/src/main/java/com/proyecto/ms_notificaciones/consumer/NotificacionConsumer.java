package com.proyecto.ms_notificaciones.consumer;

import com.proyecto.ms_notificaciones.config.RabbitNames;
import com.proyecto.ms_notificaciones.dto.NotificacionMensaje;
import com.proyecto.ms_notificaciones.exception.FalloTemporalException;
import com.proyecto.ms_notificaciones.exception.MensajeInvalidoException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Consumidor del dominio "notificaciones".
 *
 * - Exito                  -> basicAck.
 * - MensajeInvalido        -> basicNack sin requeue -> DLQ (no se reintenta).
 * - FalloTemporal          -> se republica con x-intentos+1 hasta MAX_INTENTOS;
 *                             al agotarse, basicNack sin requeue -> DLQ.
 * - Cualquier otro error   -> basicNack sin requeue -> DLQ.
 */
@Slf4j
@Component
public class NotificacionConsumer {

    private static final int MAX_INTENTOS = 3;
    private static final String HEADER_INTENTOS = "x-intentos";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitNames names;

    public NotificacionConsumer(RabbitTemplate rabbitTemplate, RabbitNames names) {
        this.rabbitTemplate = rabbitTemplate;
        this.names = names;
    }

    @RabbitListener(queues = "${app.rabbit.queue}")
    public void procesar(NotificacionMensaje mensaje, Message original, Channel channel) throws IOException {
        long tag = original.getMessageProperties().getDeliveryTag();
        int intentos = intentosDe(original);

        try {
            validar(mensaje);
            enviarNotificacion(mensaje);
            channel.basicAck(tag, false);
            log.info("Notificacion procesada: pedidoId={}", mensaje.pedidoId());

        } catch (MensajeInvalidoException e) {
            log.error("Mensaje invalido, va a DLQ: {}", e.getMessage());
            channel.basicNack(tag, false, false);

        } catch (FalloTemporalException e) {
            if (intentos + 1 >= MAX_INTENTOS) {
                log.error("Reintentos agotados ({}), va a DLQ: {}", MAX_INTENTOS, e.getMessage());
                channel.basicNack(tag, false, false);
            } else {
                log.warn("Fallo temporal, reintento {}/{}: {}", intentos + 1, MAX_INTENTOS, e.getMessage());
                republicar(original, intentos + 1);
                channel.basicAck(tag, false);
            }

        } catch (Exception e) {
            log.error("Error inesperado, va a DLQ: {}", e.getMessage(), e);
            channel.basicNack(tag, false, false);
        }
    }

    private void validar(NotificacionMensaje m) {
        if (m == null || m.pedidoId() == null || m.cliente() == null || m.cliente().isBlank()) {
            throw new MensajeInvalidoException("Faltan campos obligatorios (pedidoId, cliente)");
        }
    }

    /** Simulacion del envio. El texto "FALLO_TEMPORAL" fuerza un error recuperable para probar. */
    private void enviarNotificacion(NotificacionMensaje m) {
        if (m.mensaje() != null && m.mensaje().contains("FALLO_TEMPORAL")) {
            throw new FalloTemporalException("Servicio de envio no disponible");
        }
        log.info("Enviando notificacion a {}: {}", m.cliente(), m.mensaje());
    }

    private int intentosDe(Message message) {
        Object valor = message.getMessageProperties().getHeader(HEADER_INTENTOS);
        return valor instanceof Number n ? n.intValue() : 0;
    }

    private void republicar(Message original, int intentos) {
        original.getMessageProperties().setHeader(HEADER_INTENTOS, intentos);
        rabbitTemplate.send(names.exchange(), names.routingKey(), original);
    }
}
