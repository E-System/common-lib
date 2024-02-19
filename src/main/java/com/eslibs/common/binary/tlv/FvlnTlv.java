package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.function.Predicate;

public class FvlnTlv extends VlnTlv {

    private final int decimalCount;

    FvlnTlv(int tag, long value, int decimalCount, Predicate<Long> validator) {
        super(tag, value, validator);
        this.decimalCount = decimalCount;
    }

    @Override
    public byte[] encodeValue() throws IOException {
        byte[] full = super.encodeValue();
        byte[] res = new byte[full.length + 1];
        res[0] = (byte) decimalCount;
        System.arraycopy(full, 0, res, 1, full.length);
        return res;
    }
}
