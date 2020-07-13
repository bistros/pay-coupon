package com.bistros.pay.coupon.cpservice.api.viewmodel;

import java.time.LocalDate;

import com.bistros.pay.coupon.cpservice.application.usecase.AllocateCouponUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllocatedCouponViewModel implements ViewModel {
    private String couponId;
    private String assignUserId;
    private LocalDate expirateDate;

    public static AllocatedCouponViewModel from(AllocateCouponUseCase.AssginCouponResponse response) {
        return new AllocatedCouponViewModel(response.getCouponId(), response.getUserId(), response.getExpirationDate());
    }
}
