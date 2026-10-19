package com.proyecto.ms_admin_rabbit.dto;

import jakarta.validation.constraints.NotBlank;

public record BindingRequest(
        @NotBlank(message = "exchange es obligatorio") String exchange,
        @NotBlank(message = "queue es obligatorio") String queue,
        String routingKey) {
}
