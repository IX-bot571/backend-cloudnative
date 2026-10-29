package com.proyecto.ms_notificaciones.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Consume tickets. Exito: ACK. Cuerpo con "FALLO": NACK sin requeue -> DLQ. */
@Slf4j
@Component
public class TicketConsumer {

    @RabbitListener(queues = "${app.rabbit.tickets-queue}", containerFactory = "rawListenerContainerFactory")
    public void procesar(byte[] body, Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String cuerpo = new String(body, StandardCharsets.UTF_8);
        if (cuerpo.contains("FALLO")) {
            log.error("Ticket invalido, va a DLQ: {}", cuerpo);
            channel.basicNack(tag, false, false);
            return;
        }
        log.info("Ticket generado: {}", cuerpo);
        channel.basicAck(tag, false);
    }
}

