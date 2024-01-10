package com.eslibs.common.binary.tlv;

import com.eslibs.common.binary.Bytes;

import java.io.IOException;

public interface ITlv {

    int getTag();

    boolean isValid();

    default byte[] getBytes() throws IOException {
        byte[] v = encodeValue();
        byte[] data = new byte[4 + v.length];
        System.arraycopy(Bytes.getInt16BytesLE(getTag()), 0, data, 0, 2);
        System.arraycopy(Bytes.getInt16BytesLE(v.length), 0, data, 2, 2);
        System.arraycopy(v, 0, data, 4, v.length);
        return data;
    }

    byte[] encodeValue() throws IOException;
}
