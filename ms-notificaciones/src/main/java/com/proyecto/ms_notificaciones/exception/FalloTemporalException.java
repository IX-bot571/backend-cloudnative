package com.proyecto.ms_notificaciones.exception;

/** Error recuperable: se reintenta hasta agotar intentos y luego va a la DLQ. */
public class FalloTemporalException extends RuntimeException {
    public FalloTemporalException(String message) {
        super(message);
    }
}
