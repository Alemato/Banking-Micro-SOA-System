package it.univaq.sose.transactionserviceprosumer.service;

import java.io.Serial;

public class ServiceUnavailableException extends Exception {
    @Serial
    private static final long serialVersionUID = 7448585239797982412L;

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
