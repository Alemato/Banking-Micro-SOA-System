package it.univaq.sose.bankingoperationsserviceprosumer.service;

import java.io.Serial;

/**
 * Custom exception class to handle service unavailability scenarios.
 * This exception is thrown when a required service is unavailable.
 */
public class ServiceUnavailableException extends Exception {
    @Serial
    private static final long serialVersionUID = 4537060738671954014L;

    public ServiceUnavailableException() {
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }

    public ServiceUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
