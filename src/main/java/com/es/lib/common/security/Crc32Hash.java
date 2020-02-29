package com.es.lib.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.zip.CRC32;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Crc32Hash implements NumericHash {

    @Override
    public long create(byte[] value) {
        CRC32 crc = new CRC32();
        crc.update(value);
        return crc.getValue();
    }
}
