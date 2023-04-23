package com.eslibs.common.security;

public interface StrHash {

    String get(String value);

    String get(byte[] value);

    default boolean valid(String value, String hash) {
        if (hash == null || value == null) {
            return false;
        }
        return get(value).equals(hash);
    }
}
