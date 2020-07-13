package com.bistros.pay.coupon.operator.infra.persistence;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.bistros.pay.coupon.operator.domain.model.ExpirationCoupon;
import com.bistros.pay.coupon.operator.domain.model.GenCoupon;
import com.bistros.pay.coupon.operator.domain.model.GenCouponDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class GenCouponDaoImpl implements GenCouponDao {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertTemplate;

    @Autowired
    public GenCouponDaoImpl(DataSource dataSource) {
        this.insertTemplate = new SimpleJdbcInsert(dataSource)
            .withTableName("COUPON").usingColumns("ID", "STATE", "CREATE_DATE_TIME");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int insert(GenCoupon coupon) {
        return insert(Collections.singletonList(coupon));
    }

    public int insert(List<GenCoupon> coupons) {
        BeanPropertySqlParameterSource[] arrays =
            coupons.stream().map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        int[] results = insertTemplate.executeBatch(arrays);
        return Arrays.stream(results).sum();
    }

    @Override
    public List<ExpirationCoupon> findExpiringCoupon(LocalDate expire) {
        return jdbcTemplate
            .query("SELECT * FROM COUPON WHERE ASSIGN_USER_ID IS NOT NULL AND STATE = 'NORMAL' AND EXPIRATION_DATE = ?"
                , (rs, rowNum) -> new ExpirationCoupon(
                    rs.getString("ID"),
                    rs.getTimestamp("CREATE_DATE_TIME").toLocalDateTime(),
                    rs.getString("ASSIGN_USER_ID"),
                    rs.getTimestamp("EXPIRATION_DATE").toLocalDateTime().toLocalDate()
                ), expire);
    }
}
