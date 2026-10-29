package com.proyecto.ms_notificaciones.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Nombres de la topologia RabbitMQ (app.rabbit.*). Unica fuente de verdad. */
@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitNames(
        String exchange,
        String queue,
        String routingKey,
        String dlx,
        String dlq,
        String dlqRoutingKey,
        String ticketsQueue,
        String ticketsRoutingKey,
        String ticketsDlq,
        String ticketsDlqRoutingKey,
        String eventosExchange,
        String documentosQueue,
        String documentosBinding,
        String documentosDlq,
        String documentosDlqRoutingKey) {
}
