package it.univaq.sose.bankingoperationsserviceprosumer.service;

import java.io.Serial;

/**
 * Exception thrown when a malformed URL location is encountered.
 */
public class UrlLocationMalformedException extends Exception {
    @Serial
    private static final long serialVersionUID = 714767030489350318L;

    public UrlLocationMalformedException() {
    }

    public UrlLocationMalformedException(String message) {
        super(message);
    }

    public UrlLocationMalformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlLocationMalformedException(Throwable cause) {
        super(cause);
    }

    public UrlLocationMalformedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
