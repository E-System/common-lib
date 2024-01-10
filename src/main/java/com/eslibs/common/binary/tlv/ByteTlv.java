package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.function.Predicate;

public class ByteTlv extends AbstractTlv<Byte> {

    ByteTlv(int code, Byte value, Predicate<Byte> validator) {
        super(code, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return new byte[]{value};}
}
