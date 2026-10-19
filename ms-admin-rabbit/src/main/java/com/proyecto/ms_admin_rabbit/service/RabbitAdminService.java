package com.proyecto.ms_admin_rabbit.service;

import com.proyecto.ms_admin_rabbit.dto.BindingRequest;
import com.proyecto.ms_admin_rabbit.dto.ExchangeRequest;
import com.proyecto.ms_admin_rabbit.dto.QueueRequest;
import com.proyecto.ms_admin_rabbit.exception.RecursoNoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** Toda la logica de administracion de RabbitMQ. Los controladores solo llaman a este servicio. */
@Slf4j
@Service
public class RabbitAdminService {

    private final AmqpAdmin admin;
    private final RabbitTemplate rabbitTemplate;

    public RabbitAdminService(AmqpAdmin admin, RabbitTemplate rabbitTemplate) {
        this.admin = admin;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void crearCola(QueueRequest r) {
        QueueBuilder b = r.durable() ? QueueBuilder.durable(r.name()) : QueueBuilder.nonDurable(r.name());
        if (r.ttlMs() != null) b.ttl(r.ttlMs().intValue());
        if (r.deadLetterExchange() != null && !r.deadLetterExchange().isBlank()) {
            b.deadLetterExchange(r.deadLetterExchange());
        }
        if (r.deadLetterRoutingKey() != null && !r.deadLetterRoutingKey().isBlank()) {
            b.deadLetterRoutingKey(r.deadLetterRoutingKey());
        }
        admin.declareQueue(b.build());
        log.info("Cola creada: {}", r.name());
    }

    public void eliminarCola(String name) {
        if (!existeCola(name)) {
            throw new RecursoNoEncontradoException("La cola '" + name + "' no existe");
        }
        admin.deleteQueue(name);
        log.info("Cola eliminada: {}", name);
    }

    public void crearExchange(ExchangeRequest r) {
        AbstractExchange ex = switch (r.type()) {
            case DIRECT -> new DirectExchange(r.name(), r.durable(), false);
            case TOPIC -> new TopicExchange(r.name(), r.durable(), false);
            case FANOUT -> new FanoutExchange(r.name(), r.durable(), false);
            case HEADERS -> new HeadersExchange(r.name(), r.durable(), false);
        };
        admin.declareExchange(ex);
        log.info("Exchange creado: {} ({})", r.name(), r.type());
    }

    public void eliminarExchange(String name) {
        if (!existeExchange(name)) {
            throw new RecursoNoEncontradoException("El exchange '" + name + "' no existe");
        }
        admin.deleteExchange(name);
        log.info("Exchange eliminado: {}", name);
    }

    public void crearBinding(BindingRequest r) {
        admin.declareBinding(armar(r));
        log.info("Binding creado: {} -> {} [{}]", r.exchange(), r.queue(), r.routingKey());
    }

    public void eliminarBinding(BindingRequest r) {
        admin.removeBinding(armar(r));
        log.info("Binding eliminado: {} -> {} [{}]", r.exchange(), r.queue(), r.routingKey());
    }

    /** Declaracion pasiva de cola: solo verifica que exista, no crea nada. */
    private boolean existeCola(String name) {
        return Boolean.TRUE.equals(rabbitTemplate.execute(channel -> {
            try {
                channel.queueDeclarePassive(name);
                return true;
            } catch (IOException e) {
                return false;
            }
        }));
    }

    /** Declaracion pasiva de exchange: solo verifica que exista, no crea nada. */
    private boolean existeExchange(String name) {
        return Boolean.TRUE.equals(rabbitTemplate.execute(channel -> {
            try {
                channel.exchangeDeclarePassive(name);
                return true;
            } catch (IOException e) {
                return false;
            }
        }));
    }

    private Binding armar(BindingRequest r) {
        String key = r.routingKey() == null ? "" : r.routingKey();
        return new Binding(r.queue(), Binding.DestinationType.QUEUE, r.exchange(), key, null);
    }
}
