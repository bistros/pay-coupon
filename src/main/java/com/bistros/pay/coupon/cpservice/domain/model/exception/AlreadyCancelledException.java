package com.bistros.pay.coupon.cpservice.domain.model.exception;

public class AlreadyCancelledException extends PayServerRuntimeException {
    public AlreadyCancelledException(String message) {
        super(message);
    }
}
