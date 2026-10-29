package com.proyecto.ms_notificaciones.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/** Registra en el log los mensajes que llegaron a tickets.dlq y documentos.dlq. */
@Slf4j
@Component
public class DlqLogConsumer {

    @RabbitListener(queues = {"${app.rabbit.tickets-dlq}", "${app.rabbit.documentos-dlq}"}, ackMode = "AUTO", containerFactory = "rawListenerContainerFactory")
    public void registrar(byte[] body, @Header(AmqpHeaders.CONSUMER_QUEUE) String cola) {
        log.error("[DLQ] cola={} mensaje={}", cola, new String(body, StandardCharsets.UTF_8));
    }
}
