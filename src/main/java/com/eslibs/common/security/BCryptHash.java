package com.eslibs.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BCryptHash implements StrHash {

    private final int level;

    @Override
    public String get(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt(level));
    }

    @Override
    public String get(byte[] value) {
        return BCrypt.hashpw(String.valueOf(value), BCrypt.gensalt(level));
    }

    @Override
    public boolean valid(String value, String hash) {
        try {
            return value != null && BCrypt.checkpw(value, hash);
        } catch (IllegalArgumentException ignore) {
            return false;
        }
    }
}
