package it.univaq.sose.bankaccountservice.webservice;

import java.io.Serial;

/**
 * Exception thrown when a requested bank account is not found.
 */
public class NotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1292673996797365627L;

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
