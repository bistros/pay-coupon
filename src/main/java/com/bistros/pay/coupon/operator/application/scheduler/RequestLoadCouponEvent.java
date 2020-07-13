package com.bistros.pay.coupon.operator.application.scheduler;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestLoadCouponEvent {
    private final int queueSize;

    public RequestLoadCouponEvent(int remainCoupon) {
        queueSize = remainCoupon;
    }
}

