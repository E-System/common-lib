package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.function.Predicate;

public class ByteTlv extends AbstractTlv<Byte> {

    ByteTlv(int tag, byte value, Predicate<Byte> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return new byte[]{value};}
}
