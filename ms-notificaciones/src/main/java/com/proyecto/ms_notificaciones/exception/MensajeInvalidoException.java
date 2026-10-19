package com.proyecto.ms_notificaciones.exception;

/** Error NO recuperable: reintentar no sirve. Va directo a la DLQ. */
public class MensajeInvalidoException extends RuntimeException {
    public MensajeInvalidoException(String message) {
        super(message);
    }
}
