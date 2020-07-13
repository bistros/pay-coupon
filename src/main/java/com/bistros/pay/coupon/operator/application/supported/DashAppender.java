package com.bistros.pay.coupon.operator.application.supported;

import java.util.function.Function;

import com.bistros.pay.coupon.operator.domain.model.exception.InvalidCouponFormatException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import org.thymeleaf.util.StringUtils;

public class DashAppender implements Function<String, String> {

    @Override
    public String apply(String raw) {
        if (StringUtils.isEmpty(raw) || raw.length() != 16) {
            throw new InvalidCouponFormatException(String.format("raw:[%s]", raw));
        }
        Iterable<String> str = Splitter.fixedLength(4).split(raw);
        return Joiner.on("-").join(str);
    }
}
