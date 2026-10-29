package com.proyecto.ms_notificaciones.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Consume documentos (exchange topic). Exito: ACK. Cuerpo con "FALLO": NACK sin requeue -> DLQ. */
@Slf4j
@Component
public class DocumentoConsumer {

    @RabbitListener(queues = "${app.rabbit.documentos-queue}", containerFactory = "rawListenerContainerFactory")
    public void procesar(byte[] body, Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                         @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws IOException {
        String cuerpo = new String(body, StandardCharsets.UTF_8);
        if (cuerpo.contains("FALLO")) {
            log.error("Documento invalido, va a DLQ: {}", cuerpo);
            channel.basicNack(tag, false, false);
            return;
        }
        log.info("Documento procesado ({}): {}", routingKey, cuerpo);
        channel.basicAck(tag, false);
    }
}
