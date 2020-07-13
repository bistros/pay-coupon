package com.bistros.pay.coupon.operator.domain.service;

import java.util.List;

public interface CouponGenerator {
    List<String> generator(int size);
}
