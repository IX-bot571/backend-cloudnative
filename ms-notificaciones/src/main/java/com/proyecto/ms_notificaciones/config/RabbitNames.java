package com.proyecto.ms_notificaciones.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nombres de la topologia RabbitMQ, leidos de application.yml (app.rabbit.*).
 * Unica fuente de verdad: ninguna otra clase escribe nombres a mano.
 */
@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitNames(
        String exchange,
        String queue,
        String routingKey,
        String dlx,
        String dlq,
        String dlqRoutingKey) {
}
