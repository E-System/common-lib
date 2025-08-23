package com.eslibs.common.binary.tlv;

import com.eslibs.common.binary.Bytes;

import java.io.IOException;
import java.util.function.Predicate;

class TlvLong extends TlvBase<Long> {

    TlvLong(int tag, long value, Predicate<Long> validator) {
        super(tag, value, validator);
    }

    @Override
    public byte[] encodeValue() throws IOException {return Bytes.getLongBytesLE(value);}
}
