package com.bistros.pay.coupon.operator.application;

import com.bistros.pay.coupon.operator.configuration.GeneratorProperties;
import com.bistros.pay.coupon.operator.domain.service.CouponGenerator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class ConcurrencyCouponGenerator implements CouponGenerator {


    private final int workerSize;

    public ConcurrencyCouponGenerator(GeneratorProperties properties) {
        workerSize = properties.getWorkerSize();
    }

    @Override
    public List<String> generator(int createCount) {
        List<CompletableFuture<List<String>>> futures = new ArrayList<CompletableFuture<List<String>>>();

        for (int i = 0; i < workerSize; i++) {
            int realCreateCount = getRealSize(workerSize, createCount, i);
            CouponGeneratorWorker worker = new CouponGeneratorWorker(getSeedCharacter(i), realCreateCount);

            futures.add(CompletableFuture.supplyAsync(worker::call));

        }
        List<String> ids = futures.stream()
            .map(CompletableFuture::join).flatMap(List::stream).collect(toList());

        return ids;
    }

    int getRealSize(int workerSize, int createCount, int nodeNumber) {
        if (workerSize == nodeNumber + 1) {
            // 마지막 Thread 라면
            return createCount - (createCount / workerSize * nodeNumber);
        }
        return createCount / workerSize;
    }

    char getSeedCharacter(int index) {
        return (char) ('A' + index);
    }
}
