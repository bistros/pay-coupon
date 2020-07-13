package com.bistros.pay.coupon.cpservice.infra.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bistros.pay.coupon.cpservice.domain.model.Coupon;
import com.bistros.pay.coupon.cpservice.domain.model.CouponState;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/*
    spring-data-'jdbc' : https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/#jdbc.query-methods
 */
public interface CouponRepository extends PagingAndSortingRepository<Coupon, String> {


    Optional<Coupon> findByIdAndAndAssignUserId(String id, String assignUserId);


    List<Coupon> findByAssignUserId(String userId);
    List<Coupon> findFirst10000ByStateIs(CouponState state);

    //usecase
    List<Coupon> findByExpirationDateBetweenAndStateIsAndAssignUserIdNotNull(LocalDate expireDate, LocalDate nextDate, CouponState state);


    /*
        useTime(쿠폰 사용 요청 시각) 을 expiration time 에서도 재활용하기 때문에 DATEADD 를 사용하였다.
        example) 유효기간이 7/12인 쿠폰은 7월 12일 오후 2시에 쿠폰 사용 요청이 들어 오더라도 사용이 가능해야한다. 그래서 `유효기간+1일 > 사용요청시각` 을 위해서 DATEADD +1일을 하였다.

        CURRENT_DATE 를 쓰면 테스트에서 단점이 있고, 4번째 파라미터로 TODAY를 받으면 파라미터를 하나 더 추가해야하는 단점이 있다.
     */
    @Modifying
    @Query("UPDATE COUPON c SET c.USE_DATE_TIME=:useTime , c.STATE='USED' WHERE c.id=:couponId AND c.ASSIGN_USER_ID=:ownerId AND c.state = 'NORMAL' AND EXPIRATION_DATE >= CURRENT_DATE")
    int setCouponUsedState(String couponId, String ownerId, LocalDateTime useTime);

}
