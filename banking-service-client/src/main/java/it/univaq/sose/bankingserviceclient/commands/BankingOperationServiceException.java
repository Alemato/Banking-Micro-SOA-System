package it.univaq.sose.bankingserviceclient.commands;

import java.io.Serial;

public class BankingOperationServiceException extends Exception {
    @Serial
    private static final long serialVersionUID = -8080646048425331977L;

    public BankingOperationServiceException() {
    }

    public BankingOperationServiceException(String message) {
        super(message);
    }

    public BankingOperationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankingOperationServiceException(Throwable cause) {
        super(cause);
    }

    public BankingOperationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
