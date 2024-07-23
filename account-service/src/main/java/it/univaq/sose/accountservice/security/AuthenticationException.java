package it.univaq.sose.accountservice.security;

import java.io.Serial;

/**
 * Exception thrown when an authentication error occurs.
 */
public class AuthenticationException extends Exception {
    @Serial
    private static final long serialVersionUID = -7574209823462477590L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    protected AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
