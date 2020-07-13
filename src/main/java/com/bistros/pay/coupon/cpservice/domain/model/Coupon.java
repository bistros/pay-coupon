package com.bistros.pay.coupon.cpservice.domain.model;

import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyCancelledException;
import com.bistros.pay.coupon.cpservice.domain.model.exception.AlreadyUsedCouponException;

import lombok.*;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coupon {

    @Id
    @NonNull
    private String id;
    private CouponState state = CouponState.NORMAL;

    private String assignUserId;
    private LocalDateTime assignedDateTime;
    private LocalDateTime useDateTime;
    private LocalDate expirationDate;


    /*
        정상 쿠폰을 발급 취소하고 재사용 가능하게 만든다는 의미는, 할당 정보를 모두 삭제하라는 뜻이다.
        할당 시간,사용자정보, 유효기간을 삭제하고  쿠폰 상태는 UNASSIGN 으로 변경한다.
     */
    public void cancle() {
        switch (getState()) {
            case CANCLED:
                throw new AlreadyCancelledException(getId());
            case USED:
                throw new AlreadyUsedCouponException(getId());
        }
        this.state = CouponState.UNASSIGN;
        this.assignUserId = null;
        this.assignedDateTime = null;
        this.expirationDate = null;
    }
}
