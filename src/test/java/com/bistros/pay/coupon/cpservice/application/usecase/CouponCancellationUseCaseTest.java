package com.bistros.pay.coupon.cpservice.application.usecase;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase.CancellationRequest;
import com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase.CancellationResponse;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyCancelledException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyUsedCouponException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.NotExistCouponException;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CouponCancellationUseCaseTest {

    @Test
    @DisplayName("취소된 쿠폰을 다시 취소하려면 에러가 발생한다")
    public void testCancle1() {
        String COUPON_ID = "CANCELLED-ID";
        String userId = "TK";
        Coupon cancelledCoupon = new Coupon(COUPON_ID, CouponState.CANCLED, userId, null, null, null);

        CouponRepository mockRepository = mock(CouponRepository.class);
        when(mockRepository.findByIdAndAndAssignUserId(COUPON_ID, userId)).thenReturn(Optional.of(cancelledCoupon));

        CouponCancellationUseCase cancellationUseCase = new CouponCancellationUseCase(mockRepository);

        assertThrows(AlreadyCancelledException.class, () -> {
            cancellationUseCase.apply(CancellationRequest.builder().couponId(COUPON_ID).requestUserId(userId).build());
        });
    }

    @Test
    @DisplayName("사용된 쿠폰을 사용 하려고 하면 에러가 발생한다")
    public void testCancell2() {
        String USAGED_COUPON_ID = "USED-ID";
        String userId = "TK";
        Coupon usedCoupon = new Coupon(USAGED_COUPON_ID, CouponState.USED, userId, null, null, null);

        CouponRepository mockRepository = mock(CouponRepository.class);
        when(mockRepository.findByIdAndAndAssignUserId(USAGED_COUPON_ID, userId)).thenReturn(Optional.of(usedCoupon));

        CouponCancellationUseCase cancellationUseCase = new CouponCancellationUseCase(mockRepository);

        assertThrows(AlreadyUsedCouponException.class, () -> {
            cancellationUseCase.apply(CancellationRequest.builder().couponId(USAGED_COUPON_ID).requestUserId(userId).build());
        });
    }

    @Test
    @DisplayName("정상 쿠폰을 취소 처리요청 성공")
    public void testCancell3() {
        String NORMAL_COUPON_ID = "NORMAL-COUPON-ID";
        String userId = "TK";
        Coupon coupon = new Coupon(NORMAL_COUPON_ID, CouponState.NORMAL, userId, null, null, null);

        CouponRepository mockRepository = mock(CouponRepository.class);
        when(mockRepository.findByIdAndAndAssignUserId(NORMAL_COUPON_ID, userId)).thenReturn(Optional.of(coupon));

        CouponCancellationUseCase cancellationUseCase = new CouponCancellationUseCase(mockRepository);
        CancellationResponse result = cancellationUseCase.apply(CancellationRequest.builder().couponId(NORMAL_COUPON_ID).requestUserId(userId).build());

        assertAll(
                () -> assertEquals("NORMAL-COUPON-ID", result.getCouponId())
        );
    }

    @Test
    @DisplayName("정상 쿠폰이지만, 다른 사람의 쿠폰을 취소하려고 할 때는 쿠폰이 없다고 에러가 발생한다")
    public void expectThrowCancelTest1() {
        String NORMAL_COUPON_ID = "NORMAL-COUPON-ID";
        String onwerId = "OWNER";
        Coupon coupon = new Coupon(NORMAL_COUPON_ID, CouponState.NORMAL, onwerId, LocalDateTime.now(), null, LocalDate.now().plusDays(2));
        CouponRepository mockRepository = mock(CouponRepository.class);
        when(mockRepository.findByIdAndAndAssignUserId(NORMAL_COUPON_ID, onwerId)).thenReturn(Optional.of(coupon));

        CouponCancellationUseCase cancellationUseCase = new CouponCancellationUseCase(mockRepository);
        assertThrows(NotExistCouponException.class, () -> {
            cancellationUseCase.apply(CancellationRequest.builder().couponId(NORMAL_COUPON_ID).requestUserId("TK").build());
        });
    }
}
