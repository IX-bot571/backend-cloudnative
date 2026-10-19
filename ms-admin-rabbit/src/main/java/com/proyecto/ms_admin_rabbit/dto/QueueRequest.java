package com.proyecto.ms_admin_rabbit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record QueueRequest(
        @NotBlank(message = "name es obligatorio")
        @Size(max = 255, message = "name no puede superar 255 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "name solo admite letras, numeros, '.', '_' y '-'")
        String name,
        boolean durable,
        @Min(value = 1, message = "ttlMs debe ser mayor que 0") Long ttlMs,
        String deadLetterExchange,
        String deadLetterRoutingKey) {
}
