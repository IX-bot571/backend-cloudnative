package com.proyecto.ms_pedidos.dto;

/** Mensaje publicado en tareas.exchange. Debe coincidir con CONTRATO.md y con ms-notificaciones. */
public record NotificacionMensaje(Long pedidoId, String cliente, String mensaje) {
}
