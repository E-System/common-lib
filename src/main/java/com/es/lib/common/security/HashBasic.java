package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class HashBasic implements Hash {

    private final String algorithm;

    @Override
    public String create(String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(value.getBytes(StandardCharsets.UTF_8));
            return HashUtil.toHex(digest.digest());
        } catch (Exception ignore) {
            return null;
        }
    }
}
