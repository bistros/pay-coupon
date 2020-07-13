package com.bistros.pay.coupon.cpservice.application.usecase;

import java.time.LocalDateTime;

import com.bistros.pay.coupon.cpservice.application.shared.RequestUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.ResponseUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.UseCase;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.exception.NotExistCouponException;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class CouponCancellationUseCase
        implements UseCase<CouponCancellationUseCase.CancellationRequest, CouponCancellationUseCase.CancellationResponse> {


    private final CouponRepository couponRepository;


    @Override
    public CancellationResponse apply(CancellationRequest request) {
        Coupon coupon = couponRepository.findByIdAndAndAssignUserId(request.couponId, request.requestUserId)
                .orElseThrow(() -> new NotExistCouponException(request.couponId));

        coupon.cancle();
        couponRepository.save(coupon);

        return CancellationResponse.of(request.couponId, LocalDateTime.now());
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CancellationRequest implements RequestUseCase {
        private final String couponId;
        private final String requestUserId;
    }

    @Value(staticConstructor = "of")
    public static class CancellationResponse implements ResponseUseCase {
        String couponId;
        LocalDateTime cancleTime;
    }
}
