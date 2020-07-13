package com.bistros.pay.coupon.operator.application.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class ExpireDateNotifierScheduler {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 아침 10시마다 할당되어 미사용이지만, 만료기간이 3일 남은 쿠폰을 출력한다
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void notifier() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate expire = now.plusDays(3).toLocalDate();
        eventPublisher.publishEvent(new RequestCheckExpireDateEvent(now, expire));
    }
}
