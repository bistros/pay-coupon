package com.bistros.pay.coupon.operator.infra.persistence;

import com.bistros.pay.coupon.operator.domain.model.GenCoupon;
import com.bistros.pay.coupon.operator.infra.persistence.supported.WithInsert;

import org.springframework.data.repository.CrudRepository;

public interface GenCouponRepository extends CrudRepository<GenCoupon, String>, WithInsert<GenCoupon> {



}
