package com.bistros.pay.coupon.operator.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@ConfigurationPropertiesScan("com.bistros.pay.coupon.operator")
@EnableJdbcRepositories(basePackages = "com.bistros.pay.coupon.operator.infra")
public class OperatorConfiguration {
}
