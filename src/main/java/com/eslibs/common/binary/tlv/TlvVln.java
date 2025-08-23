package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

public class TlvVln extends TlvLong {

    TlvVln(int tag, long value, Predicate<Long> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {
        byte[] full = super.encodeValue();
        int index = full.length - 1;
        do {
            if (full[index] != 0x0) {
                break;
            }
            --index;
        } while (index >= 0);
        index += 1;
        if (index == 0) {
            return new byte[]{0x0};
        }
        return Arrays.copyOf(full, index);
    }
}
