package it.univaq.sose.loanserviceprosumer.service;

import java.io.Serial;

public class LoanNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 7702504782550515074L;

    public LoanNotFoundException() {
    }

    public LoanNotFoundException(String message) {
        super(message);
    }

    public LoanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanNotFoundException(Throwable cause) {
        super(cause);
    }

    public LoanNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
