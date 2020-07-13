package com.bistros.pay.coupon.cpservice.domain.model.exception;

public class PayServerRuntimeException extends RuntimeException {

    public PayServerRuntimeException() {
    }

    public PayServerRuntimeException(String message) {
        super(message);
    }

    public PayServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayServerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
