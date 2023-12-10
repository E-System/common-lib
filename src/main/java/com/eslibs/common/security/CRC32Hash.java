package com.eslibs.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.zip.CRC32;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CRC32Hash implements CRCHash {

    @Override
    public long of(byte[] value) {
        CRC32 crc = new CRC32();
        crc.update(value);
        return crc.getValue();
    }
}
