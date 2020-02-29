package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HashBCrypt implements Hash {

    private final int level;

    @Override
    public String create(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt(level));
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
