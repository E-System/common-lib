package com.eslibs.common.security;

import com.eslibs.common.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BasicHash implements StrHash {

    private final String algorithm;

    @Override
    public String get(String value) {
        if (value == null) {
            return null;
        }
        return get(Constant.bytes(value));
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
