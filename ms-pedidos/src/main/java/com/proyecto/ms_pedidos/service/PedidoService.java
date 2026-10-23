package com.proyecto.ms_pedidos.service;

import com.proyecto.ms_pedidos.dto.NotificacionMensaje;
import com.proyecto.ms_pedidos.dto.PedidoRequest;
import com.proyecto.ms_pedidos.entity.Pedido;
import com.proyecto.ms_pedidos.producer.NotificacionProducer;
import com.proyecto.ms_pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final NotificacionProducer producer;

    public PedidoService(PedidoRepository repository, NotificacionProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Long crear(PedidoRequest request) {
        Pedido p = new Pedido();
        p.setCliente(request.cliente());
        p.setProducto(request.producto());
        p.setCantidad(request.cantidad());
        Pedido guardado = repository.save(p);
        producer.enviar(new NotificacionMensaje(guardado.getId(), request.cliente(),
                "Pedido recibido: " + request.cantidad() + " x " + request.producto()));
        return guardado.getId();
    }

    public List<Pedido> listar() {
        return repository.findAll();
    }
}
