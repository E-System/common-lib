package com.es.lib.common.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class CacheProvider {

    private static final long DEFAULT_TTL = 20 * 60 * 1000;

    private final Map<String, Item<?>> items = new ConcurrentHashMap<>();

    public <T> T computeIfAbsent(String key, Supplier<T> supplier) {
        return computeIfAbsent(key, null, supplier);
    }

    public <T> T computeIfAbsent(String key, Long ttl, Supplier<T> supplier) {
        return (T) items.computeIfAbsent(
            key,
            v -> createData(supplier.get(), ttl)
        ).getData();
    }

    public <T, R> R computeIfAbsent(String key, Supplier<T> supplier, Function<T, R> wrapper) {
        return computeIfAbsent(key, null, supplier, wrapper);
    }

    public <T, R> R computeIfAbsent(String key, Long ttl, Supplier<T> supplier, Function<T, R> wrapper) {
        return wrapper.apply(computeIfAbsent(key, ttl, supplier));
    }

    public <T> T put(String key, T data) {
        return put(key, data, null);
    }

    public <T> T put(String key, T data, Long ttl) {
        Item<T> cacheData = createData(data, ttl);
        items.putIfAbsent(key, cacheData);
        return data;
    }

    public <T> T get(String key) {
        Item<?> data = items.get(key);
        return data != null ? (T) data.getData() : null;
    }

    private <T> Item<T> createData(T data, Long ttl) {
        return new Item<>(new Date().getTime() + (ttl != null ? ttl : DEFAULT_TTL), data);
    }

    public void clean() {
        long currentTime = new Date().getTime();
        Iterator<Map.Entry<String, Item<?>>> iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Item<?>> entry = iterator.next();
            Item<?> data = entry.getValue();
            if (currentTime > data.getTtl()) {
                iterator.remove();
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Item<T> {

        private final Long ttl;
        private final T data;
    }
}

