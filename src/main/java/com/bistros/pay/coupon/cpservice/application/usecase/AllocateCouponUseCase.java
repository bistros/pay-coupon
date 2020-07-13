package com.bistros.pay.coupon.cpservice.application.usecase;

import com.bistros.pay.coupon.cpservice.application.service.CouponPreloadQueue;
import com.bistros.pay.coupon.cpservice.application.shared.RequestUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.ResponseUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.UseCase;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;
import com.bistros.pay.coupon.cpservice.domain.model.exception.CouponOutOfStockException;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AllocateCouponUseCase implements UseCase<AllocateCouponUseCase.AssignCouponRequest, AllocateCouponUseCase.AssginCouponResponse> {

    private final CouponPreloadQueue couponQueue;
    private final CouponRepository couponRepository;

    /**
     * 기 생성된 쿠폰을 사용자에게 할당한다.
     * ASSIGN_USER, ASSIGN_DATE, STATE 가 변경이 된다.
     */
    @Override
    public AssginCouponResponse apply(AssignCouponRequest request) {
        Coupon queuedCoupon = couponQueue.getCoupon().orElseThrow(CouponOutOfStockException::new);
        LocalDateTime now = LocalDateTime.now();

        Coupon coupon = new Coupon(queuedCoupon.getId(), CouponState.NORMAL, request.getUserId(), now, null, expirationDate(now.toLocalDate()));

        Coupon result = couponRepository.save(coupon);

        return new AssginCouponResponse(result.getAssignUserId(), result.getId(), result.getExpirationDate());
    }


    /**
     * '할당일 기준' 7일의 유효기간 으로 설정하기 위한 메소드이다.
     **/
    static final int COUPON_EXPIRE_DATE_RANGE_DAYS = 7;

    public LocalDate expirationDate(LocalDate startExpireDate) {
        return startExpireDate.plusDays(COUPON_EXPIRE_DATE_RANGE_DAYS);
    }


    @Getter
    @AllArgsConstructor
    public static class AssignCouponRequest implements RequestUseCase {
        private final String userId;
    }

    @Getter
    @AllArgsConstructor
    public static class AssginCouponResponse implements ResponseUseCase {
        private final String userId;
        private final String couponId;
        private final LocalDate expirationDate;
    }
}
