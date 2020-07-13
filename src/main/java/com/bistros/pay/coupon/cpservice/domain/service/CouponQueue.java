package com.bistros.pay.coupon.cpservice.domain.service;

import com.bistros.pay.coupon.cpservice.domain.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponQueue {
    int getRemainCoupon();

    boolean appendCoupon(List<Coupon> coupons);

    Optional<Coupon> getCoupon();
}
