package com.bistros.pay.coupon.cpservice.api.viewmodel;

import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponApprovalViewModel implements ViewModel {
    private String couponId;


    public static CouponApprovalViewModel from(CopuonApprovalUseCase.CouponApprovalResponse response) {
        return new CouponApprovalViewModel(response.getCouponId());
    }
}
