package com.eslibs.common.binary.tlv;

import com.eslibs.common.binary.Bytes;

import java.io.IOException;
import java.util.function.Predicate;

public class TlvInt extends TlvBase<Integer> {

    TlvInt(int tag, int value, Predicate<Integer> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return Bytes.getInt32BytesLE(value);}
}
