package com.bistros.pay.coupon.cpservice.domain.model.exception;

public class FailedUseCouponException extends PayServerRuntimeException {
    public FailedUseCouponException(String message) {
        super(message);
    }
}
