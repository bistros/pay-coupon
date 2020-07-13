package com.bistros.pay.coupon.cpservice.api.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponViewModel implements ViewModel {
    private final String couponId;


    private final String state;

    private final String assignUserId;


}
