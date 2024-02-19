package com.eslibs.common.binary.tlv;

import com.eslibs.common.binary.Bytes;

import java.io.IOException;

public interface ITlv {

    int getTag();

    boolean isValid();

    default byte[] getBytes() throws IOException {
        byte[] v = encodeValue();
        byte[] result = new byte[4 + v.length];
        System.arraycopy(Bytes.getInt16BytesLE(getTag()), 0, result, 0, 2);
        System.arraycopy(Bytes.getInt16BytesLE(v.length), 0, result, 2, 2);
        System.arraycopy(v, 0, result, 4, v.length);
        return result;
    }

    byte[] encodeValue() throws IOException;
}
