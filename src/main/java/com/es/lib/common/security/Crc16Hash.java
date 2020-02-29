package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Crc16Hash implements NumericHash {

    @Override
    public long create(byte[] value) {
        int crc = 0xFFFF;
        for (byte v : value) {
            crc = ((crc >>> 8) | (crc << 8)) & 0xFFFF;
            crc ^= (v & 0xFF);
            crc ^= ((crc & 0xFF) >> 4);
            crc ^= (crc << 12) & 0xFFFF;
            crc ^= ((crc & 0xFF) << 5) & 0xFFFF;
        }
        crc &= 0xFFFF;
        return crc;
    }
}
