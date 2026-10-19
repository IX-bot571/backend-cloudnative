package com.proyecto.ms_admin_rabbit.controller;

import com.proyecto.ms_admin_rabbit.dto.ExchangeRequest;
import com.proyecto.ms_admin_rabbit.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exchanges")
public class ExchangeController {

    private final RabbitAdminService service;

    public ExchangeController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@Valid @RequestBody ExchangeRequest request) {
        service.crearExchange(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("exchange", request.name()));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> eliminar(@PathVariable String name) {
        service.eliminarExchange(name);
        return ResponseEntity.noContent().build();
    }
}
