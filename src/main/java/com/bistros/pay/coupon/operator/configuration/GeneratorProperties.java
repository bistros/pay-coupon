package com.bistros.pay.coupon.operator.configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Validated
@ConfigurationProperties("generator")
public class GeneratorProperties {

    @Min(1) @Max(25)
    private int workerSize = 10;
}
