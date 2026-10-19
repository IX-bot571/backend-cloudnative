package com.proyecto.ms_pedidos.service;

import com.proyecto.ms_pedidos.dto.NotificacionMensaje;
import com.proyecto.ms_pedidos.dto.PedidoRequest;
import com.proyecto.ms_pedidos.producer.NotificacionProducer;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PedidoService {

    // TEMPORAL: en la Fase 5 el id lo genera la BD (PedidoRepository).
    private final AtomicLong secuencia = new AtomicLong(1);
    private final NotificacionProducer producer;

    public PedidoService(NotificacionProducer producer) {
        this.producer = producer;
    }

    public Long crear(PedidoRequest request) {
        Long id = secuencia.getAndIncrement();
        producer.enviar(new NotificacionMensaje(id, request.cliente(),
                "Pedido recibido: " + request.cantidad() + " x " + request.producto()));
        return id;
    }
}
