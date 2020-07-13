package com.bistros.pay.coupon.operator.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.bistros.pay.coupon.operator.configuration.GeneratorProperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExpirationConcurrencyCouponGeneratorTest {

    @Test
    @DisplayName("1만개 쿠폰을 3개로 나누어서 생성하려면 3333,3333,3334 이어야한다")
    public void test1() {
        ConcurrencyCouponGenerator container = new ConcurrencyCouponGenerator(mock(GeneratorProperties.class));
        assertAll(
            () -> assertEquals(3333, container.getRealSize(3, 10000, 0)),
            () -> assertEquals(3333, container.getRealSize(3, 10000, 1)),
            () -> assertEquals(3334, container.getRealSize(3, 10000, 2))
        );
    }


    @Test
    @DisplayName("2개 노드에서 100개 쿠폰은 50,50이 만들어져야 한다")
    public void test2() {
        ConcurrencyCouponGenerator container = new ConcurrencyCouponGenerator(mock(GeneratorProperties.class));
        assertAll(
            () -> assertEquals(50, container.getRealSize(2, 100, 0)),
            () -> assertEquals(50, container.getRealSize(2, 100, 1))
        );
    }

    @Test
    @DisplayName("4개 노드에서 33 쿠폰은 8,8,8,9")
    public void test3() {
        ConcurrencyCouponGenerator container = new ConcurrencyCouponGenerator(mock(GeneratorProperties.class));
        assertAll(
            () -> assertEquals(8, container.getRealSize(4, 33, 0)),
            () -> assertEquals(8, container.getRealSize(4, 33, 1)),
            () -> assertEquals(8, container.getRealSize(4, 33, 2)),
            () -> assertEquals(9, container.getRealSize(4, 33, 3))
        );
    }

}
