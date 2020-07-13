package com.bistros.pay.coupon.cpservice.domain.model;

public enum CouponState {

    NORMAL("사용가능"), CANCLED("취"), USED("사용됨"), UNASSIGN("미발행");

    private String description;

    CouponState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
