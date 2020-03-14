package com.es.lib.common.number;

import com.es.lib.common.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NumberConverter {

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
        return value != null ? Math.round(value.doubleValue() * Constant.SCALED_LONG_MULTIPLIER) : 0;
    }

    public BigDecimal toBigDecimal() {
        long longValue = asLong();
        return longValue != 0 ? BigDecimal.valueOf(longValue, Constant.DEFAULT_SCALE) : null;
    }
}
