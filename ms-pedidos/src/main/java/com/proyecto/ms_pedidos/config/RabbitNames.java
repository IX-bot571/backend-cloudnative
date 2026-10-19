package com.proyecto.ms_pedidos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Nombres de RabbitMQ leidos de application.yml (app.rabbit.*). Unica fuente de verdad. */
@ConfigurationProperties(prefix = "app.rabbit")
public record RabbitNames(
        String exchange,
        String notificacionRoutingKey) {
}
