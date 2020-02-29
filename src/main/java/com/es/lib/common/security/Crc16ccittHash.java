package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Crc16ccittHash implements NumericHash {

    private final int skipIndex;
    private final int skipLen;

    @Override
    public long create(byte[] value) {
        int crc = 0xFFFF;          // init
        int polynom = 0x1021;   // 0001 0000 0010 0001
        for (int i = 0; i < value.length; i++) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            for (int j = 0; j < 8; j++) {
                boolean bit = ((value[i] >> (7 - j) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynom;
            }
        }
        crc &= 0xFFFF;
        return crc;
    }
}
