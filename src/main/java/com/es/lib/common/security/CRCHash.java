package com.es.lib.common.security;

import java.nio.charset.StandardCharsets;

public interface CRCHash {

    default long get(String value) {
        return get(value.getBytes(StandardCharsets.UTF_8));
    }

    long get(byte[] value);

    default boolean valid(String value, long hash) {
        return get(value) == hash;
    }

    default boolean valid(byte[] value, long hash) {
        return get(value) == hash;
    }

    static CRCHash crc32() {
        return new CRC32Hash();
    }

    static CRCHash crc16() {
        return new CRC16Hash();
    }

    static CRCHash crc16ccitt() {
        return crc16ccitt(0, 0);
    }

    static CRCHash crc16ccitt(int skipIndex, int skipLen) {
        return new CRC16ccittHash(skipIndex, skipLen);
    }
}
