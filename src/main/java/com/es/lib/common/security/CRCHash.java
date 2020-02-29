package com.es.lib.common.security;

import java.nio.charset.StandardCharsets;

public interface CRCHash {

    default long get(String value) {
        return get(value.getBytes(StandardCharsets.UTF_8));
    }

    long get(byte[] value);

    default boolean valid(String value, long hash) {
        return get(value) == hash;
    }

    default boolean valid(byte[] value, long hash) {
        return get(value) == hash;
    }
}
