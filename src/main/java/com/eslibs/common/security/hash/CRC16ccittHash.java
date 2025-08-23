package com.eslibs.common.security.hash;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CRC16ccittHash implements CRCHash {

    private final int skipIndex;
    private final int skipLen;
    private final int init;

    @Override
    public long of(byte[] value) {
        int crc = init;
        int polynomial = 0x1021;   // 0001 0000 0010 0001
        for (int i = 0; i < value.length; ++i) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            for (int j = 0; j < 8; ++j) {
                boolean bit = ((value[i] >> (7 - j) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xFFFF;
        return crc;
    }
}
