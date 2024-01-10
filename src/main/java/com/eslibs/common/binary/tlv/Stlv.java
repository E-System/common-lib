package com.eslibs.common.binary.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;

class Stlv extends AbstractTlv<Collection<? extends ITlv>> {

    Stlv(int tag, Collection<? extends ITlv> value, Predicate<Collection<? extends ITlv>> validator) {super(tag, value, validator);}

    @Override
    public byte[] encodeValue() throws IOException {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            for (ITlv v : value) {
                result.write(v.getData());
            }
            return result.toByteArray();
        }
    }
}
