package com.bistros.pay.coupon.cpservice.application.service;

import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.service.CouponQueue;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Component
public class CouponPreloadQueue implements CouponQueue {

    private final Queue<Coupon> queue = new ConcurrentLinkedDeque<>();

    @Override
    public int getRemainCoupon() {
        return queue.size();
    }

    @Override
    public boolean appendCoupon(List<Coupon> coupons) {
        log.info("현재 큐에는 {} 개의 쿠폰이 남아 있으며, {} 개가 추가됩니다", getRemainCoupon(), coupons.size());
        return queue.addAll(coupons);
    }

    @Override
    public Optional<Coupon> getCoupon() {
        return Optional.ofNullable(queue.poll());
    }
}
