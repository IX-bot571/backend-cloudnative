package com.proyecto.ms_notificaciones.dto;

/**
 * Mensaje de notificacion que ms-pedidos publica en tareas.exchange.
 * Debe coincidir con el contrato (CONTRATO.md).
 */
public record NotificacionMensaje(
        Long pedidoId,
        String cliente,
        String mensaje) {
}
