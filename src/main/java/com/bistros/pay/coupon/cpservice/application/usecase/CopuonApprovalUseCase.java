package com.bistros.pay.coupon.cpservice.application.usecase;

import java.time.LocalDateTime;

import com.bistros.pay.coupon.cpservice.application.shared.RequestUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.ResponseUseCase;
import com.bistros.pay.coupon.cpservice.application.shared.UseCase;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyCancelledException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyUsedCouponException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.FailedUseCouponException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.NotExistCouponException;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CopuonApprovalUseCase implements UseCase<CopuonApprovalUseCase.CouponApprovalRequest, CopuonApprovalUseCase.CouponApprovalResponse> {

    private CouponRepository repository;

    @Override
    public CouponApprovalResponse apply(CouponApprovalRequest request) {
        LocalDateTime requestUseTime = LocalDateTime.now();
        int updated = repository.setCouponUsedState(request.couponId, request.requestUserId, requestUseTime);
        if (updated > 0) {
            return new CouponApprovalResponse(request.couponId, requestUseTime);
        }

        Coupon coupon = repository.findByIdAndAndAssignUserId(request.couponId, request.requestUserId)
                .orElseThrow(() -> new NotExistCouponException(request.couponId));

        switch (coupon.getState()) {
            case CANCLED:
                throw new AlreadyCancelledException(coupon.getId());
            case USED:
                throw new AlreadyUsedCouponException(coupon.getId() + " was used  " + coupon.getUseDateTime());
        }
        log.warn("[{}] : {}", request.couponId, coupon.toString());
        throw new FailedUseCouponException(request.couponId + " by " + request.requestUserId);
    }

    @Getter
    @AllArgsConstructor
    public static class CouponApprovalRequest implements RequestUseCase {
        private final String couponId;
        private final String requestUserId;
    }

    @Getter
    @AllArgsConstructor
    public static class CouponApprovalResponse implements ResponseUseCase {
        private final String couponId;
        private final LocalDateTime useTime;
    }
}
