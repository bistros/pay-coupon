package com.bistros.pay.coupon.cpservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJdbcRepositories(basePackages = "com.bistros.pay.coupon.cpservice")
public class ServiceConfiguration {
}
