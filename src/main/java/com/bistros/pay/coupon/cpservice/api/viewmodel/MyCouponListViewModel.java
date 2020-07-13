package com.bistros.pay.coupon.cpservice.api.viewmodel;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.bistros.pay.coupon.cpservice.application.usecase.GetAssignedCouponUseCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCouponListViewModel implements ViewModel{

    private final List<CouponViewModel> coupons;

    public static MyCouponListViewModel from(GetAssignedCouponUseCase.GetAssignedCouponsResponse response) {
        List<CouponViewModel> list = response.getCouponList().stream().map(c ->
                CouponViewModel.builder()
                        .couponId(c.getId())
                        .state(c.getState().getDescription()).build()).collect(toList());

        return new MyCouponListViewModel(list);
    }
}
