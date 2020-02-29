package com.es.lib.common.security;

import java.nio.charset.StandardCharsets;

public interface NumericHash {

    default long create(String value) {
        return create(value.getBytes(StandardCharsets.UTF_8));
    }

    long create(byte[] value);

    default boolean valid(String value, long hash) {
        return create(value) == hash;
    }

    default boolean valid(byte[] value, long hash) {
        return create(value) == hash;
    }
}
