package com.bistros.pay.coupon.cpservice.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AllocateCouponUseCaseTest {

    @Test
    @DisplayName("2020-01-01, 10시10분에 생성된 쿠폰은 만료일이 2020-01-07 이다 (즉 7일 23시59분 59초)")
    public void testCreateExpirationDate2() {
        AllocateCouponUseCase allocateCouponUseCase = new AllocateCouponUseCase(null,null);

        LocalDateTime now = LocalDateTime.of(2020, 1, 1, 10, 10, 00);
        LocalDate expirationDate = allocateCouponUseCase.expirationDate(now.toLocalDate());


        assertEquals(expirationDate, LocalDate.of(2020, 1, 8));
    }

}
