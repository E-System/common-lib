package com.es.lib.common.security;

public interface Hash {

    String create(String value);

    default boolean valid(String value, String hash) {
        if (hash == null || value == null) {
            return false;
        }
        return create(value).equals(hash);
    }
}
