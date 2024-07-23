package it.univaq.sose.bankaccountservice.webservice;

import java.io.Serial;

/**
 * Exception thrown when an attempt is made to create a bank account that already exists for th account.
 */
public class BankAccountAlradyExistException extends Exception {
    @Serial
    private static final long serialVersionUID = 2696173958797365627L;

    public BankAccountAlradyExistException() {
        super();
    }

    public BankAccountAlradyExistException(String message) {
        super(message);
    }

    public BankAccountAlradyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankAccountAlradyExistException(Throwable cause) {
        super(cause);
    }

    protected BankAccountAlradyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
