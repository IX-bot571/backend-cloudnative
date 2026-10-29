package com.proyecto.ms_notificaciones.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fabrica de listeners para colas con cuerpo de texto plano (tickets y documentos).
 * Usa SimpleMessageConverter: no interpreta JSON, entrega el cuerpo tal cual.
 */
@Configuration
public class RawListenerConfig {

    @Bean
    SimpleRabbitListenerContainerFactory rawListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(connectionFactory);
        f.setMessageConverter(new SimpleMessageConverter());
        f.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        f.setDefaultRequeueRejected(false);
        return f;
    }
}
