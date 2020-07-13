package com.bistros.pay.coupon.operator.application.scheduler;

import com.bistros.pay.coupon.cpservice.application.service.CouponPreloadQueue;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class CouponQueueCheckScheduler {

    private final ApplicationEventPublisher eventPublisher;

    //cpservice 쪽에 있는 Queue 클래스이다. 실제 서비스에서는 RabbitMQ 등을 고려한다
    private final CouponPreloadQueue queue;

    @Scheduled(cron = "*/5 * * * * *")
    public void loadCoupon() {
        if (queue.getRemainCoupon() < 10000) {
            eventPublisher.publishEvent(new RequestLoadCouponEvent(queue.getRemainCoupon()));
        }
    }
}
