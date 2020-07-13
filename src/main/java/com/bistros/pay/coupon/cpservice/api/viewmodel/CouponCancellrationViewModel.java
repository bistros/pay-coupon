package com.bistros.pay.coupon.cpservice.api.viewmodel;

import java.time.LocalDateTime;

import com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CouponCancellrationViewModel implements ViewModel{
    private String couponId;
    private LocalDateTime cancelTime;

    public static CouponCancellrationViewModel from(CouponCancellationUseCase.CancellationResponse response) {
        return new CouponCancellrationViewModel(response.getCouponId(), LocalDateTime.now());
    }
}
