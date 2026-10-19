package com.proyecto.ms_admin_rabbit.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitAdminConfig {

    // Boot ya define un AmqpAdmin; este bean explicito solo se crea si no existe otro.
    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(AmqpAdmin.class)
    RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }
}
