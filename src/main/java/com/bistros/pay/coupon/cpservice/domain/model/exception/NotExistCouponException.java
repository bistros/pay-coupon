package com.bistros.pay.coupon.cpservice.domain.model.exception;

public class NotExistCouponException extends PayServerRuntimeException {

    public NotExistCouponException(String id) {
        super(id);
    }
}
