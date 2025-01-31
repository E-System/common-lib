package com.eslibs.common.binary.tlv;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class Tlv {

    public static ITlv from(int tag, byte value) {
        return from(tag, value, (Predicate<Byte>) null);
    }

    public static ITlv from(int tag, byte value, Predicate<Byte> validator) {
        return new ByteTlv(tag, value, validator);
    }

    public static ITlv from(int tag, short value) {
        return from(tag, value, (Predicate<Short>) null);
    }

    public static ITlv from(int tag, short value, Predicate<Short> validator) {
        return new ShortTlv(tag, value, validator);
    }

    public static ITlv from(int tag, int value) {
        return from(tag, value, null);
    }

    public static ITlv from(int tag, int value, Predicate<Integer> validator) {
        return new IntTlv(tag, value, validator);
    }

    public static ITlv from(int tag, long value, boolean vln) {
        return from(tag, value, vln, null);
    }

    public static ITlv from(int tag, long value, boolean vln, Predicate<Long> validator) {
        if (vln) {
            return new VlnTlv(tag, value, validator);
        }
        return new LongTlv(tag, value, validator);
    }

    public static ITlv from(int tag, String value) {
        return from(tag, value, StringTlv.DEFAULT_CHARSET);
    }

    public static ITlv from(int tag, String value, Charset charset) {
        return from(tag, value, charset, null);
    }

    public static ITlv from(int tag, String value, Charset charset, Predicate<String> validator) {
        return new StringTlv(tag, value, charset, validator);
    }

    public static ITlv from(int tag, ITlv... items) {
        return from(tag, null, items);
    }

    public static ITlv from(int tag, Predicate<Collection<? extends ITlv>> validator, ITlv... items) {
        return new Stlv(tag, Arrays.asList(items), validator);
    }
}
