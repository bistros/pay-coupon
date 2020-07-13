package com.bistros.pay.coupon.cpservice.application.shared;

import java.util.function.Function;

public interface UseCase<T extends RequestUseCase, R extends ResponseUseCase> extends Function<T, R> {

    @Override
    R apply(T t);
}
