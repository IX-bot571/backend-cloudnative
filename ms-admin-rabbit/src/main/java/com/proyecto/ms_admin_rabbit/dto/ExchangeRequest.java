package com.proyecto.ms_admin_rabbit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExchangeRequest(
        @NotBlank(message = "name es obligatorio")
        @Size(max = 255, message = "name no puede superar 255 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "name solo admite letras, numeros, '.', '_' y '-'")
        String name,
        @NotNull(message = "type es obligatorio (DIRECT, TOPIC, FANOUT, HEADERS)") ExchangeKind type,
        boolean durable) {

    public enum ExchangeKind { DIRECT, TOPIC, FANOUT, HEADERS }
}
