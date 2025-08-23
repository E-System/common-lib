package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.function.Predicate;

public class TlvByte extends TlvBase<Byte> {

    TlvByte(int tag, byte value, Predicate<Byte> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return new byte[]{value};}
}
