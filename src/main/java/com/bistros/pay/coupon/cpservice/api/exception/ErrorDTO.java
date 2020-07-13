package com.bistros.pay.coupon.cpservice.api.exception;

import com.bistros.pay.coupon.cpservice.api.viewmodel.ViewModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDTO implements ViewModel {
    private String ex;
    private String message;
}
