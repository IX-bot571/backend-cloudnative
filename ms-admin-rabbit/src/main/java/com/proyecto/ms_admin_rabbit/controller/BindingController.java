package com.proyecto.ms_admin_rabbit.controller;

import com.proyecto.ms_admin_rabbit.dto.BindingRequest;
import com.proyecto.ms_admin_rabbit.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bindings")
public class BindingController {

    private final RabbitAdminService service;

    public BindingController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> crear(@Valid @RequestBody BindingRequest request) {
        service.crearBinding(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar(@Valid @RequestBody BindingRequest request) {
        service.eliminarBinding(request);
        return ResponseEntity.noContent().build();
    }
}
