package com.bistros.pay.coupon.operator.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface GenCouponDao {
    int insert(GenCoupon coupon);
    int insert(List<GenCoupon> coupons);
    List<ExpirationCoupon> findExpiringCoupon(LocalDate expire);
}
