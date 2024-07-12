package it.univaq.sose.transactionserviceprosumer.service;

import java.io.Serial;

public class AccountServiceException extends Exception {
    @Serial
    private static final long serialVersionUID = -8080646048425331977L;

    public AccountServiceException() {
    }

    public AccountServiceException(String message) {
        super(message);
    }

    public AccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountServiceException(Throwable cause) {
        super(cause);
    }

    public AccountServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
