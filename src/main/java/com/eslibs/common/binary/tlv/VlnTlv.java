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
        int index = 0;
        do {
            if (full[index] == 0x0) {
                break;
            }
            ++index;
        } while (index < full.length);
        return Arrays.copyOf(full, index);
    }
}
