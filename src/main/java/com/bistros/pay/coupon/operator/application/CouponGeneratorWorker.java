package com.bistros.pay.coupon.operator.application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bistros.pay.coupon.operator.application.supported.CouponPrefixGenerator;
import com.bistros.pay.coupon.operator.application.supported.DashAppender;
import com.bistros.pay.coupon.operator.application.supported.ValidCodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponGeneratorWorker {

    private final Character WORKER_SEEDNAME;
    private final int size;

    private final DashAppender dashAppender = new DashAppender();
    private final ValidCodeGenerator validCodeGenerator = new ValidCodeGenerator();
    private final CouponPrefixGenerator prefixGenerator = new CouponPrefixGenerator();


    public CouponGeneratorWorker(char SEED, int size) {
        this.WORKER_SEEDNAME = SEED;
        this.size = size;
    }


    public String generator() {
        return prefixGenerator
            .andThen(validCodeGenerator)
            .andThen(dashAppender)
            .apply(String.valueOf(WORKER_SEEDNAME));
    }

    public List<String> call() {
        List<String> res = IntStream.range(0, size).parallel()
            .mapToObj(c -> generator()).collect(Collectors.toList());

        log.info("{} 워커에서 {} 개를 생성하엿습니다", WORKER_SEEDNAME, res.size());
        return res;
    }
}
