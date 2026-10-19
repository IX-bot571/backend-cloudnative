package com.proyecto.ms_admin_rabbit.controller;

import com.proyecto.ms_admin_rabbit.dto.QueueRequest;
import com.proyecto.ms_admin_rabbit.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/queues")
public class QueueController {

    private final RabbitAdminService service;

    public QueueController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@Valid @RequestBody QueueRequest request) {
        service.crearCola(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("queue", request.name()));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> eliminar(@PathVariable String name) {
        service.eliminarCola(name);
        return ResponseEntity.noContent().build();
    }
}
