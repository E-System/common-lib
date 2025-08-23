package com.eslibs.common.security.hash;

public interface StrHash {

    String of(String value);

    String of(byte[] value);

    default boolean valid(String value, String hash) {
        if (hash == null || value == null) {
            return false;
        }
        return of(value).equals(hash);
    }
}
