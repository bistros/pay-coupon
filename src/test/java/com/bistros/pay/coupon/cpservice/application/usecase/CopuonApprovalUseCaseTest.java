package com.bistros.pay.coupon.cpservice.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase.CouponApprovalRequest;
import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase.CouponApprovalResponse;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyUsedCouponException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.NotExistCouponException;
import com.bistros.pay.coupon.cpservice.infra.persistence.CouponRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CopuonApprovalUseCaseTest {

    @Test
    @DisplayName("사용가능한 내 쿠폰을 내사 사용 요청하면, 정상처리되어야함")
    public void testApprovalTest1() {
        String NORMAL_COUPON_ID = "NORMAL-COUPON";
        String couponOwnerAndRequestId = "TK";

        // setup : 정상 쿠폰을 사용 요청 하면 "1"이 반환된다.
        CouponRepository repository = mock(CouponRepository.class);
        when(repository.setCouponUsedState(NORMAL_COUPON_ID, couponOwnerAndRequestId, LocalDateTime.now())).thenReturn(1);

        CopuonApprovalUseCase useCase = new CopuonApprovalUseCase(repository);
        CouponApprovalRequest request = new CouponApprovalRequest(NORMAL_COUPON_ID, couponOwnerAndRequestId);
        CouponApprovalResponse response = useCase.apply(request);

        assertEquals(NORMAL_COUPON_ID, response.getCouponId());
    }

    /**
     * `다른 사람의 쿠폰입니다` 라고 표현하는 것은 과도한 메시지이다. 이 경우에도 ' 내 쿠폰이 없다' 정도로 표현한다.
     */
    @Test
    @DisplayName("다른 사람의 정상 쿠폰을 사용하려고 할 때, 쿠폰이 없다고 오류가 발생")
    public void testThrowNotExistMyCoupon() {
        String NORMAL_COUPON_ID = "NORMAL-COUPON";
        String COUPON_OWNER = "TK";
        String REQUEST_USER = "ANONYMOUS";
        Coupon tkCoupon = new Coupon(NORMAL_COUPON_ID, CouponState.NORMAL, COUPON_OWNER, null, null, null);

        CouponRepository repository = mock(CouponRepository.class);
        when(repository.setCouponUsedState(NORMAL_COUPON_ID, REQUEST_USER, null)).thenReturn(0);
        when(repository.findByIdAndAndAssignUserId(NORMAL_COUPON_ID, REQUEST_USER)).thenReturn(Optional.empty());
        when(repository.findByIdAndAndAssignUserId(NORMAL_COUPON_ID, COUPON_OWNER)).thenReturn(Optional.of(tkCoupon));

        CopuonApprovalUseCase useCase = new CopuonApprovalUseCase(repository);
        CouponApprovalRequest request = new CouponApprovalRequest(NORMAL_COUPON_ID, REQUEST_USER);

        assertThrows(NotExistCouponException.class, () -> useCase.apply(request));
    }

    @Test
    @DisplayName("쿠폰이 없는데 사용 요청을 할 경우 오류가 발생함")
    public void testThrowNotExistApproval2() {
        String NORMAL_COUPON_ID = "NOT-EXIST--COUPON";
        String REQUEST_USER_ID = "TK";

        CouponRepository repository = mock(CouponRepository.class);
        when(repository.setCouponUsedState(NORMAL_COUPON_ID, REQUEST_USER_ID, null)).thenReturn(0);
        when(repository.findByIdAndAndAssignUserId(NORMAL_COUPON_ID, REQUEST_USER_ID)).thenReturn(Optional.empty());

        CopuonApprovalUseCase useCase = new CopuonApprovalUseCase(repository);
        CouponApprovalRequest request = new CouponApprovalRequest(NORMAL_COUPON_ID, REQUEST_USER_ID);

        assertThrows(NotExistCouponException.class, () -> useCase.apply(request));
    }

    @Test
    @DisplayName("이미 사용된 내 쿠폰을 다시 사용 하려고 할 경우 오류가 발생함")
    public void testThrowALreadyUsedApproval() {
        String USED_MY_COUPON = "USED_MY_COUPON";
        String REQUEST_USER_ID = "TK";
        Coupon usedCoupon = new Coupon(USED_MY_COUPON, CouponState.USED, REQUEST_USER_ID, null, LocalDateTime.of(2020, 7, 1, 10, 0, 0), null);

        CouponRepository repository = mock(CouponRepository.class);
        when(repository.setCouponUsedState(USED_MY_COUPON, REQUEST_USER_ID, null)).thenReturn(0);
        when(repository.findByIdAndAndAssignUserId(USED_MY_COUPON, REQUEST_USER_ID)).thenReturn(Optional.of(usedCoupon));

        CopuonApprovalUseCase useCase = new CopuonApprovalUseCase(repository);
        CouponApprovalRequest request = new CouponApprovalRequest(USED_MY_COUPON, REQUEST_USER_ID);

        assertThrows(AlreadyUsedCouponException.class, () -> useCase.apply(request));
    }

}
