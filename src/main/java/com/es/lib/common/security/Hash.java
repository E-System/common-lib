package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Hash {

    private static final String ALGORITHM_SHA1 = "SHA-1";
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA256 = "SHA256";

    public static StrHash bcrypt() {
        return bcrypt(12);
    }

    public static StrHash bcrypt(int level) {
        return new BCryptHash(level);
    }

    public static StrHash md5() {
        return hash(ALGORITHM_MD5);
    }

    public static StrHash sha1() {
        return hash(ALGORITHM_SHA1);
    }

    public static StrHash hash(String algorithm) {
        return new BasicHash(algorithm);
    }

    public static StrHash hmacSha256(String secret) {
        return hmac(ALGORITHM_SHA256, secret);
    }

    public static StrHash hmac(String algorithm, String secret) {
        return new HmacHash(algorithm, secret);
    }

    public static CRCHash crc32() {
        return new CRC32Hash();
    }

    public static CRCHash crc16() {
        return new CRC16Hash();
    }

    public static CRCHash crc16ccitt() {
        return crc16ccitt(0, 0);
    }

    public static CRCHash crc16ccitt(int skipIndex, int skipLen) {
        return new CRC16ccittHash(skipIndex, skipLen);
    }
}
