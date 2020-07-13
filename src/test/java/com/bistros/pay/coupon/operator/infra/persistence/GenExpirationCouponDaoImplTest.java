package com.bistros.pay.coupon.operator.infra.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.bistros.pay.coupon.operator.domain.model.ExpirationCoupon;
import com.bistros.pay.coupon.operator.domain.model.GenCoupon;
import com.bistros.pay.coupon.operator.domain.model.GenCouponDao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@ComponentScan
@Tag("integration")
public class GenExpirationCouponDaoImplTest {


    @Autowired
    GenCouponDao genCouponDao;
    @Autowired
    GenCouponRepository genCouponRepository;


    @Test
    @DisplayName("JDBC 와 DATA-JDBC 가 H2에서 동작하는지 확인해본다")
    public void test1() {
        GenCoupon g1 = new GenCoupon("AAAA-BBBB-CCCC-DDDD", LocalDateTime.now());
        GenCoupon g2 = new GenCoupon("AAAA-BBBB-CCCC-EEEE", LocalDateTime.now());
        int res = genCouponDao.insert(Arrays.asList(g1, g2));

        assertAll(
            () -> assertEquals(2, res),
            () -> assertEquals("AAAA-BBBB-CCCC-DDDD", genCouponRepository.findById("AAAA-BBBB-CCCC-DDDD").orElseThrow(RuntimeException::new).getId()),
            () -> assertEquals("AAAA-BBBB-CCCC-EEEE", genCouponRepository.findById("AAAA-BBBB-CCCC-EEEE").orElseThrow(RuntimeException::new).getId())
        );
    }

    @Test
    @DisplayName("DATA-JBDC에  Annotation 을 이용해서 Insert 기능을 확인한다")
    public void test2() {
        GenCoupon g1 = new GenCoupon("AAAA-BBBB-CCCC-DDDD", LocalDateTime.now());
        GenCoupon inserted = genCouponRepository.insert(g1);

        assertEquals(g1.getId(), inserted.getId());
    }

    @Test
    @DisplayName("3일 이후에 만료되는 데이터를 정상적으로 가져온다")
    public void test3() {
        LocalDate localDate = LocalDate.of(2020, 7, 16);

        List<ExpirationCoupon> expected = genCouponDao.findExpiringCoupon(localDate);

        assertTrue(expected.size() == 1);
    }
}
