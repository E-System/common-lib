package com.eslibs.common.security;

import com.eslibs.common.binary.Bytes;

public interface CRCHash {

    default long of(String value) {
        return of(Bytes.of(value));
    }

    long of(byte[] value);

    default boolean valid(String value, long hash) {
        return of(value) == hash;
    }

    default boolean valid(byte[] value, long hash) {
        return of(value) == hash;
    }
}
