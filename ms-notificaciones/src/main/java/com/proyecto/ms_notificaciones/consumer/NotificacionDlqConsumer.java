package com.proyecto.ms_notificaciones.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/** Registra en el log los mensajes que terminaron en la DLQ. */
@Slf4j
@Component
public class NotificacionDlqConsumer {

    @RabbitListener(queues = "${app.rabbit.dlq}", ackMode = "AUTO")
    public void registrarFallido(Message message) {
        String cuerpo = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("[DLQ] Mensaje no entregado: {} | cabeceras={}",
                cuerpo, message.getMessageProperties().getHeaders());
    }
}
