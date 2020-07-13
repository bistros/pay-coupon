package com.bistros.pay.coupon.operator.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bistros.pay.coupon.cpservice.application.service.CouponPreloadQueue;
import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;
import com.bistros.pay.coupon.operator.application.scheduler.RequestCheckExpireDateEvent;
import com.bistros.pay.coupon.operator.application.scheduler.RequestLoadCouponEvent;
import com.bistros.pay.coupon.operator.domain.service.CouponGenerator;
import com.bistros.pay.coupon.operator.domain.model.GenCoupon;
import com.bistros.pay.coupon.operator.domain.model.GenCouponDao;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GenCouponService {

    private final GenCouponDao couponDao;
    private final CouponGenerator couponGenerator;
    private final CouponPreloadQueue queue;

    public int create(int count) {
        List<String> ids = couponGenerator.generator(count);
        return save(ids);
    }

    public int save(List<String> ids) {
        List<GenCoupon> genCoupons = ids.stream().map(c ->
            new GenCoupon(c, LocalDateTime.now())).collect(Collectors.toList());

        List<Coupon> serviceCoupon = genCoupons.stream().map(c -> new Coupon(c.getId(), CouponState.UNASSIGN, null, null, null, null))
            .collect(Collectors.toList());
        queue.appendCoupon(serviceCoupon);
        return couponDao.insert(genCoupons);
    }

    @EventListener
    public void load(RequestLoadCouponEvent event) {
        log.warn("현재 큐에 남아 있는 쿠폰의 수는 {} 입니다 추가 발행이 필요합니다.", event.getQueueSize());
    }

    @EventListener
    public void notifier(RequestCheckExpireDateEvent event) {
        System.out.println(event);
    }

}
