package it.univaq.sose.financialreportserviceprosumer.service;

import java.io.Serial;

public class FinancialServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7007626310592963995L;

    public FinancialServiceException() {
        super();
    }

    public FinancialServiceException(String message) {
        super(message);
    }

    public FinancialServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinancialServiceException(Throwable cause) {
        super(cause);
    }

    protected FinancialServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
