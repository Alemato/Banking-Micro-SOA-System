package it.univaq.sose.bankingserviceclient.commands;

import java.io.Serial;

public class BankingClientException extends Exception {
    @Serial
    private static final long serialVersionUID = -8080646048425331977L;

    public BankingClientException() {
    }

    public BankingClientException(String message) {
        super(message);
    }

    public BankingClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankingClientException(Throwable cause) {
        super(cause);
    }

    public BankingClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
