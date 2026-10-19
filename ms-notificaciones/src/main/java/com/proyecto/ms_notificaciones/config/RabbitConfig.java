package com.proyecto.ms_notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topologia RabbitMQ del caso de uso "notificaciones".
 *
 * Ruta principal:
 *   tareas.exchange --(notificacion.enviar)--> notificaciones.queue
 * Ruta de fallos (dead letter):
 *   notificaciones.queue --(NACK sin requeue)--> tareas.dlx --(notificacion.fallida)--> notificaciones.dlq
 */
@Configuration
public class RabbitConfig {

    // --- Ruta principal ---

    @Bean
    DirectExchange tareasExchange(RabbitNames names) {
        return new DirectExchange(names.exchange());
    }

    @Bean
    Queue notificacionesQueue(RabbitNames names) {
        return QueueBuilder.durable(names.queue())
                .deadLetterExchange(names.dlx())
                .deadLetterRoutingKey(names.dlqRoutingKey())
                .build();
    }

    @Bean
    Binding notificacionesBinding(Queue notificacionesQueue, DirectExchange tareasExchange, RabbitNames names) {
        return BindingBuilder.bind(notificacionesQueue).to(tareasExchange).with(names.routingKey());
    }

    // --- Ruta de fallos (DLQ) ---

    @Bean
    DirectExchange dlxExchange(RabbitNames names) {
        return new DirectExchange(names.dlx());
    }

    @Bean
    Queue notificacionesDlq(RabbitNames names) {
        return QueueBuilder.durable(names.dlq()).build();
    }

    @Bean
    Binding dlqBinding(Queue notificacionesDlq, DirectExchange dlxExchange, RabbitNames names) {
        return BindingBuilder.bind(notificacionesDlq).to(dlxExchange).with(names.dlqRoutingKey());
    }

    // --- Conversor JSON ---

    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
