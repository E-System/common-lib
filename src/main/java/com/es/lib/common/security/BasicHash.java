package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BasicHash implements StrHash {

    private final String algorithm;

    @Override
    public String get(String value) {
        if (value == null) {
            return null;
        }
        return get(value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String get(byte[] value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(value);
            return Hex.get(digest.digest());
        } catch (Exception ignore) {
            return null;
        }
    }
}
