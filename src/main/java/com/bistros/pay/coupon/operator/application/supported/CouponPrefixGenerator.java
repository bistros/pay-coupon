package com.bistros.pay.coupon.operator.application.supported;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CouponPrefixGenerator implements Function<String, String> {

    final static char[] CHARACTERS = "ABCDEFGHJIKLMNOPQRSTUVWXYZ01234567890".toCharArray();
    final static int CONSTANT_SIZE = CHARACTERS.length;


    final int GENERATED_CHAR_LENGTH = 13;
    final Random random = new Random();

    @Override
    public String apply(String seed) {
        return seed + IntStream.range(0, GENERATED_CHAR_LENGTH)
                .mapToObj(c -> CHARACTERS[(Math.abs(random.nextInt())) % CONSTANT_SIZE])
                .map(String::valueOf).collect(Collectors.joining());

    }
}
