package com.bistros.pay.coupon.cpservice.api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.bistros.pay.coupon.cpservice.api")
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ErrorDTO defaultException(Throwable e) {
        log.warn("{} ", e.getMessage(), e);
        return new ErrorDTO(e.getClass().getSimpleName(), e.getMessage());
    }

}
