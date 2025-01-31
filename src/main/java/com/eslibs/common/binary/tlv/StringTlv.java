package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Predicate;

public class StringTlv extends AbstractTlv<String> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("cp866");

    private final Charset charset;

    StringTlv(int tag, String value, Charset charset, Predicate<String> validator) {
        super(tag, value, validator);
        this.charset = charset;
    }

    @Override
    public byte[] encodeValue() throws IOException {return value.getBytes(charset);}
}
