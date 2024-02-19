package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

public class VlnTlv extends LongTlv {

    VlnTlv(int tag, Long value, Predicate<Long> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {
        byte[] full = super.encodeValue();
        int index = full.length - 1;
        while (index >= 0) {
            if (full[index] != 0x0) {
                return Arrays.copyOf(full, index);
            }
            --index;
        }
        return new byte[]{0x0};
    }
}
