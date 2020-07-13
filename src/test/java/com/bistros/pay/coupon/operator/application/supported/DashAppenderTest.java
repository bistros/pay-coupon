package com.bistros.pay.coupon.operator.application.supported;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DashAppenderTest {

    @Test
    @DisplayName("16자리 쿠폰 문자열을 4자리씩 '-'로 연결해본다")
    public void testAddDash() {
        DashAppender appender = new DashAppender();
        assertAll(
                () -> assertEquals("1234-5678-1234-5678", appender.apply("1234567812345678")),
                () -> assertEquals("ABCD-5678-ABCD-5678", appender.apply("ABCD5678ABCD5678"))
        );
    }

}
