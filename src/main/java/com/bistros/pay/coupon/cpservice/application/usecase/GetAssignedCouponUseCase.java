package com.bistros.pay.coupon.cpservice.application.usecase;

import java.util.List;

import com.bistros.pay.coupon.cpservice.application.shared.RequestUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.ResponseUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.UseCase;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GetAssignedCouponUseCase implements UseCase<GetAssignedCouponUseCase.GetAssignedCouponsRequest, GetAssignedCouponUseCase.GetAssignedCouponsResponse> {

    private final CouponRepository repository;

    @Override
    public GetAssignedCouponsResponse apply(GetAssignedCouponsRequest request) {
        List<Coupon> coupons = repository.findByAssignUserId(request.getRequestUserId());
        return new GetAssignedCouponsResponse(coupons);
    }

    @Getter
    @AllArgsConstructor
    public static class GetAssignedCouponsRequest implements RequestUseCase {
        private final String requestUserId;
    }

    @Getter
    @AllArgsConstructor
    public static class GetAssignedCouponsResponse implements ResponseUseCase {
        private final List<Coupon> couponList;
    }
}
