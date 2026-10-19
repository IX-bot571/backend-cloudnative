package com.proyecto.ms_pedidos.producer;

import com.proyecto.ms_pedidos.config.RabbitNames;
import com.proyecto.ms_pedidos.dto.NotificacionMensaje;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/** Publica notificaciones en tareas.exchange con la routing key notificacion.enviar. */
@Slf4j
@Component
public class NotificacionProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitNames names;

    public NotificacionProducer(RabbitTemplate rabbitTemplate, RabbitNames names) {
        this.rabbitTemplate = rabbitTemplate;
        this.names = names;
    }

    public void enviar(NotificacionMensaje mensaje) {
        rabbitTemplate.convertAndSend(names.exchange(), names.notificacionRoutingKey(), mensaje);
        log.info("Notificacion publicada: pedidoId={}", mensaje.pedidoId());
    }
}
