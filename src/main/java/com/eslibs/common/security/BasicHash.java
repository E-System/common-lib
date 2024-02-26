package com.eslibs.common.security;

import com.eslibs.common.binary.Bytes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.util.HexFormat;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BasicHash implements StrHash {

    private final String algorithm;

    @Override
    public String of(String value) {
        if (value == null) {
            return null;
        }
        return of(Bytes.of(value));
    }

    @Override
    public String of(byte[] value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(value);
            return HexFormat.of().formatHex(digest.digest());
        } catch (Exception ignore) {
            return null;
        }
    }
}
