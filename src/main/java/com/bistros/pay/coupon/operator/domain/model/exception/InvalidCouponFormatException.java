package com.bistros.pay.coupon.operator.domain.model.exception;

public class InvalidCouponFormatException extends PayInternalException {

    public InvalidCouponFormatException(String message) {
        super(message);
    }
}
