package com.bistros.pay.coupon.operator.api.viewmodel;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RandomGenerateViewModel {

    private final int size;
    private final LocalDateTime requestTime;
    private final LocalDateTime completedTime;


    public Duration getElasped() {
        return Duration.between(requestTime, completedTime);
    }
}
