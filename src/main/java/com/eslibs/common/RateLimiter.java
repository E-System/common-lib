package com.eslibs.common;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class RateLimiter {

    private final Supplier<Long> rateProvider;
    private Long lastOperation = null;

    public RateLimiter(long duration, TimeUnit timeUnit) {
        this(() -> timeUnit.toMillis(duration));
    }

    public void acquire() {
        final long delay = rateProvider.get();
        if (delay > 0 && lastOperation != null) {
            long diff = System.currentTimeMillis() - lastOperation;
            long wait = delay - diff;
            if (diff < delay) {
                Sys.sleep(wait);
            }
        }
        lastOperation = System.currentTimeMillis();
    }
}
