package com.bistros.pay.coupon.operator.domain.model;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
public class ExpirationCoupon extends GenCoupon {
    private String assignUserId;
    private LocalDate expirationDate;

    public ExpirationCoupon(String id, LocalDateTime createDate, String assignUserId, LocalDate expirationDate) {
        super(id, createDate);
        this.assignUserId = assignUserId;
        this.expirationDate = expirationDate;
    }
}
