package com.eslibs.common.number;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NumberConverter {

    /**
     * Default decimal count (Example 10.12)
     */
    public static final int DEFAULT_DECIMAL_COUNT = 2;
    public static final int DEFAULT_SCALE = 6;
    public static final int PRECISED_SCALE = 7;
    public static final long SCALED_LONG_MULTIPLIER = 1000000;

    private final Number value;

    public short asShort() {
        return asShort((short) 0);
    }

    public short asShort(short defaultValue) {
        return value != null ? value.shortValue() : defaultValue;
    }

    public Short asShortObject() {
        return value != null ? value.shortValue() : null;
    }

    public int asInt() {
        return asInt(0);
    }

    public int asInt(int defaultValue) {
        return value != null ? value.intValue() : defaultValue;
    }

    public Integer asIntObject() {
        return value != null ? value.intValue() : null;
    }

    public long asLong() {
        return asLong(0L);
    }

    public long asLong(long defaultValue) {
        return value != null ? value.longValue() : defaultValue;
    }

    public Long asLongObject() {
        return value != null ? value.longValue() : null;
    }

    public float asFloat() {
        return asFloat(0.0f);
    }

    public float asFloat(float defaultValue) {
        return value != null ? value.floatValue() : defaultValue;
    }

    public Float asFloatObject() {
        return value != null ? value.floatValue() : null;
    }

    public double asDouble() {
        return asDouble(0.0d);
    }

    public double asDouble(double defaultValue) {
        return value != null ? value.doubleValue() : defaultValue;
    }

    public Double asDoubleObject() {
        return value != null ? value.doubleValue() : null;
    }

    public long asScaledLong() {
        return value != null ? Math.round(value.doubleValue() * SCALED_LONG_MULTIPLIER) : 0;
    }

    public BigDecimal toBigDecimal() {
        long longValue = asLong();
        return longValue != 0 ? BigDecimal.valueOf(longValue, DEFAULT_SCALE) : null;
    }
}
