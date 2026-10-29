package com.proyecto.ms_notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Casos de uso adicionales.
 * Tickets:    tareas.exchange (direct) --ticket.generar--> tickets.queue   --fallo--> tareas.dlx --> tickets.dlq
 * Documentos: eventos.exchange (topic) --documento.*-----> documentos.queue --fallo--> tareas.dlx --> documentos.dlq
 */
@Configuration
public class RabbitConfigAmpliacion {

    // ---- Tickets (direct) ----
    @Bean
    Queue ticketsQueue(RabbitNames n) {
        return QueueBuilder.durable(n.ticketsQueue())
                .deadLetterExchange(n.dlx()).deadLetterRoutingKey(n.ticketsDlqRoutingKey()).build();
    }

    @Bean
    Queue ticketsDlq(RabbitNames n) {
        return QueueBuilder.durable(n.ticketsDlq()).build();
    }

    @Bean
    Binding ticketsBinding(Queue ticketsQueue, @Qualifier("tareasExchange") DirectExchange ex, RabbitNames n) {
        return BindingBuilder.bind(ticketsQueue).to(ex).with(n.ticketsRoutingKey());
    }

    @Bean
    Binding ticketsDlqBinding(Queue ticketsDlq, @Qualifier("dlxExchange") DirectExchange dlx, RabbitNames n) {
        return BindingBuilder.bind(ticketsDlq).to(dlx).with(n.ticketsDlqRoutingKey());
    }

    // ---- Documentos (topic) ----
    @Bean
    TopicExchange eventosExchange(RabbitNames n) {
        return new TopicExchange(n.eventosExchange());
    }

    @Bean
    Queue documentosQueue(RabbitNames n) {
        return QueueBuilder.durable(n.documentosQueue())
                .deadLetterExchange(n.dlx()).deadLetterRoutingKey(n.documentosDlqRoutingKey()).build();
    }

    @Bean
    Queue documentosDlq(RabbitNames n) {
        return QueueBuilder.durable(n.documentosDlq()).build();
    }

    @Bean
    Binding documentosBinding(Queue documentosQueue, TopicExchange eventosExchange, RabbitNames n) {
        return BindingBuilder.bind(documentosQueue).to(eventosExchange).with(n.documentosBinding());
    }

    @Bean
    Binding documentosDlqBinding(Queue documentosDlq, @Qualifier("dlxExchange") DirectExchange dlx, RabbitNames n) {
        return BindingBuilder.bind(documentosDlq).to(dlx).with(n.documentosDlqRoutingKey());
    }
}
