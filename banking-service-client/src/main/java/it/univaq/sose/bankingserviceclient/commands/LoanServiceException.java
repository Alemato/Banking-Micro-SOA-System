package it.univaq.sose.bankingserviceclient.commands;

import java.io.Serial;

public class LoanServiceException extends Exception {
    @Serial
    private static final long serialVersionUID = -8080646048425331977L;

    public LoanServiceException() {
    }

    public LoanServiceException(String message) {
        super(message);
    }

    public LoanServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanServiceException(Throwable cause) {
        super(cause);
    }

    public LoanServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
