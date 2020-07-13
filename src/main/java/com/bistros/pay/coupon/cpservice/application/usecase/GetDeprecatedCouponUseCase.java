package com.bistros.pay.coupon.cpservice.application.usecase;

import com.bistros.pay.coupon.cpservice.application.shared.EmptyRequestUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.ResponseUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.UseCase;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.bistros.pay.coupon.cpservice.application.usecase.GetDeprecatedCouponUseCase.GetDeprecatedCouponListUseCaseResponse;

@Slf4j
@Service
@AllArgsConstructor
public class GetDeprecatedCouponUseCase implements UseCase<EmptyRequestUseCase, GetDeprecatedCouponListUseCaseResponse> {

    private final CouponRepository couponRepository;

    /**
     * 당일 쿠폰이 만료된 쿠폰을 반환한다 (즉, expiration_date 가 어제 날자를 가지는 쿠폰 )
     */
    @Override
    public GetDeprecatedCouponListUseCaseResponse apply(EmptyRequestUseCase empty) {
        /* 오늘 만료된 쿠폰은, 유효기간이 어제까지인 쿠폰이다 by tk 2020.07.12 */
        LocalDate expirationDate = LocalDate.now().minusDays(1);
        List<Coupon> couponList = couponRepository.findByExpirationDateBetweenAndStateIsAndAssignUserIdNotNull(expirationDate, expirationDate.plusDays(1), CouponState.NORMAL);
        return new GetDeprecatedCouponListUseCaseResponse(expirationDate, couponList);
    }

    @Getter
    @AllArgsConstructor
    public static class GetDeprecatedCouponListUseCaseResponse implements ResponseUseCase {
        private final LocalDate expirationDate;
        private final List<Coupon> deprecatedCoupons;
    }
}
