package com.bistros.pay.coupon.operator.domain.model.exception;

public class PayInternalException extends RuntimeException {
    public PayInternalException() {
    }

    public PayInternalException(String message) {
        super(message);
    }

    public PayInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayInternalException(Throwable cause) {
        super(cause);
    }

    public PayInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
