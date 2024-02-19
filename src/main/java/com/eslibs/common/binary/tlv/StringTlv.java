package com.eslibs.common.binary.tlv;

import java.io.IOException;
import java.util.function.Predicate;

public class StringTlv extends AbstractTlv<String> {

    public static final String DEFAULT_ENCODING = "cp866";

    private final String charsetName;

    StringTlv(int tag, String value, String charsetName, Predicate<String> validator) {
        super(tag, value, validator);
        this.charsetName = charsetName;
    }

    @Override
    public byte[] encodeValue() throws IOException {return value.getBytes(charsetName);}
}
