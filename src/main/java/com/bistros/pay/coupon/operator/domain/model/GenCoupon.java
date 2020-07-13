package com.bistros.pay.coupon.operator.domain.model;

import com.bistros.pay.coupon.cpservice.domain.model.CouponState;

import lombok.Getter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 'COUPON' 테이블을 보지만 'operator' 서비스는 '쿠폰의 생성'만을 담당하
 */
@Getter
@ToString
@Table("COUPON")
public class GenCoupon {

    @Id
    protected String id;
    protected CouponState state = CouponState.UNASSIGN;
    protected LocalDateTime createDateTime;

    public GenCoupon() {
    }

    public GenCoupon(String id, LocalDateTime createDate) {
        this.id = id;
        this.createDateTime = createDate;
        this.state = CouponState.UNASSIGN;
    }


}
