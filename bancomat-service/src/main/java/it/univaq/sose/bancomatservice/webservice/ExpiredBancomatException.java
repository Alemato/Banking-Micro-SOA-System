package it.univaq.sose.bancomatservice.webservice;

import java.io.Serial;

public class ExpiredBancomatException extends Exception {
    @Serial
    private static final long serialVersionUID = -8854097433627315768L;

    public ExpiredBancomatException() {
        super();
    }

    public ExpiredBancomatException(String message) {
        super(message);
    }

    public ExpiredBancomatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredBancomatException(Throwable cause) {
        super(cause);
    }

    protected ExpiredBancomatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
