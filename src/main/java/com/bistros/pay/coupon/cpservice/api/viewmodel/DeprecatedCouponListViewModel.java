package com.bistros.pay.coupon.cpservice.api.viewmodel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.bistros.pay.coupon.cpservice.application.usecase.GetDeprecatedCouponUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeprecatedCouponListViewModel implements ViewModel{

    private LocalDate expiration_date;
    private int count;
    private final List<CouponViewModel> coupons;


    public static DeprecatedCouponListViewModel from(GetDeprecatedCouponUseCase.GetDeprecatedCouponListUseCaseResponse response) {
        List<CouponViewModel> cc = response.getDeprecatedCoupons().stream().map(c ->
                CouponViewModel.builder()
                        .assignUserId(c.getAssignUserId())
                        .couponId(c.getId())
                        .build()).collect(Collectors.toList());

        return new DeprecatedCouponListViewModel(response.getExpirationDate(), cc.size(), cc);
    }
}
