package com.bistros.pay.coupon.operator.application.supported;

import java.util.function.Function;

import com.bistros.pay.coupon.operator.domain.model.exception.InvalidCouponFormatException;

import org.thymeleaf.util.StringUtils;

/**
 * 쿠폰 16자리 중 마지막 2자리는 검증 코드로 활용한다.
 * 첫 번째 자리 : 앞의 14개의 문자열의 char -> int  -> sum 하여 % modular
 */
public class ValidCodeGenerator implements Function<String, String> {
    final static char[] VALID_CHARS = "ZTWO0O1L".toCharArray();
    final static int VALID_CHARS_SIZE = VALID_CHARS.length;


    @Override
    public String apply(String prefix) {
        if (StringUtils.isEmpty(prefix) || prefix.length() != 14) {
            throw new InvalidCouponFormatException(String.format("raw:[%s]", prefix));
        }
        char char1 = VALID_CHARS[prefix.chars().sum() % VALID_CHARS_SIZE];
        char char2 = VALID_CHARS[prefix.charAt(11) % VALID_CHARS_SIZE];
        return prefix + char1 + char2;
    }
}
