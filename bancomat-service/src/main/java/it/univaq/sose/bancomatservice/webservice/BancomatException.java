package it.univaq.sose.bancomatservice.webservice;

import java.io.Serial;

public class BancomatException extends Exception {
    @Serial
    private static final long serialVersionUID = -7324324164919886429L;

    public BancomatException() {
    }

    public BancomatException(String message) {
        super(message);
    }

    public BancomatException(String message, Throwable cause) {
        super(message, cause);
    }

    public BancomatException(Throwable cause) {
        super(cause);
    }

    public BancomatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
