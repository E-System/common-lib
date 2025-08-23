package com.eslibs.common.security.hash;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class XorHash implements CRCHash {

    private final int skipIndex;
    private final int skipLen;
    private final int lastIdx;

    @Override
    public long of(byte[] value) {
        byte res = 0x00;
        int size = lastIdx >= 0 ? lastIdx : value.length;
        for (int i = 0; i < size; ++i) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            res ^= value[i];
        }
        return res;
    }
}
