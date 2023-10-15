package com.eslibs.common.security;

import com.eslibs.common.Constant;

public interface CRCHash {

    default long get(String value) {
        return get(Constant.bytes(value));
    }

    long get(byte[] value);

    default boolean valid(String value, long hash) {
        return get(value) == hash;
    }

    default boolean valid(byte[] value, long hash) {
        return get(value) == hash;
    }
}
