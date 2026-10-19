package com.proyecto.ms_pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PedidoRequest(
        @NotBlank(message = "cliente es obligatorio") String cliente,
        @NotBlank(message = "producto es obligatorio") String producto,
        @Min(value = 1, message = "cantidad debe ser al menos 1") int cantidad) {
}
