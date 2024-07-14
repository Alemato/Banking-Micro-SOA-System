package it.univaq.sose.bancomatservice.webservice;

import java.io.Serial;

public class BancomatAlreadyExistingException extends BancomatException {
    @Serial
    private static final long serialVersionUID = -7603657738968082959L;

    public BancomatAlreadyExistingException() {
    }

    public BancomatAlreadyExistingException(String message) {
        super(message);
    }

    public BancomatAlreadyExistingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BancomatAlreadyExistingException(Throwable cause) {
        super(cause);
    }

    public BancomatAlreadyExistingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
