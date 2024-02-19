package com.eslibs.common.binary.tlv;

import com.eslibs.common.binary.Bytes;

import java.io.IOException;
import java.util.function.Predicate;

public class ShortTlv extends AbstractTlv<Short> {

    ShortTlv(int tag, short value, Predicate<Short> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return Bytes.getInt16BytesLE(value);}
}
