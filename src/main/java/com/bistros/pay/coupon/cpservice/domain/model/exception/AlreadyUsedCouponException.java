package com.bistros.pay.coupon.cpservice.domain.model.exception;

public class AlreadyUsedCouponException extends PayServerRuntimeException {

    public AlreadyUsedCouponException(String couponId) {
        super(couponId);
    }

}
