package it.univaq.sose.bancomatservice.webservice;

import java.io.Serial;

public class BancomatAlradyExistingException extends Exception {
    @Serial
    private static final long serialVersionUID = -7603657738968082959L;

    public BancomatAlradyExistingException() {
        super();
    }

    public BancomatAlradyExistingException(String message) {
        super(message);
    }

    public BancomatAlradyExistingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BancomatAlradyExistingException(Throwable cause) {
        super(cause);
    }

    protected BancomatAlradyExistingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
