package com.bistros.pay.coupon.operator.application.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class RequestCheckExpireDateEvent {

    private LocalDateTime requestDate;
    private LocalDate expireDate;
}
