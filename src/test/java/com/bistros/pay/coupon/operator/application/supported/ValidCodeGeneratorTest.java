package com.bistros.pay.coupon.operator.application.supported;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bistros.pay.coupon.operator.domain.model.exception.InvalidCouponFormatException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValidCodeGeneratorTest {

    @Test
    @DisplayName("쿠폰의 마지막 2자리 검증 생성")
    public void suffixGenerateTest1() {
        ValidCodeGenerator codeGenerator = new ValidCodeGenerator();
        assertAll(
                () -> assertEquals("12341234123412T0", codeGenerator.apply("12341234123412")),
                () -> assertEquals("A2T4AA341Z9O12WL", codeGenerator.apply("A2T4AA341Z9O12")),
                () -> assertEquals("A240AZZ41Z9OZPOL", codeGenerator.apply("A240AZZ41Z9OZP"))
        );
    }

    @Test
    @DisplayName("쿠폰의 마지막 2자리를 생성할 때 14자리가 생성되어 있어야 함")
    public void suffixGenerateTest12() {
        ValidCodeGenerator codeGenerator = new ValidCodeGenerator();
        assertThrows(InvalidCouponFormatException.class, () -> codeGenerator.apply("8COUPONS"));
    }
}
