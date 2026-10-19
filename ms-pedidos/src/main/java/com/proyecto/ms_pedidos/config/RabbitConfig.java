package com.proyecto.ms_pedidos.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion RabbitMQ del productor.
 * Ruta: ms-pedidos --(tareas.exchange, notificacion.enviar)--> notificaciones.queue
 * La cola y la DLQ las declara el consumidor (ms-notificaciones).
 */
@Configuration
public class RabbitConfig {

    @Bean
    DirectExchange tareasExchange(RabbitNames names) {
        return new DirectExchange(names.exchange());
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
