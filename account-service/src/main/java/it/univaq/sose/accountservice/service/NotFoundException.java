package it.univaq.sose.accountservice.service;

import java.io.Serial;

/**
 * Exception thrown when a requested account is not found.
 */
public class NotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 4000668156583620859L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
