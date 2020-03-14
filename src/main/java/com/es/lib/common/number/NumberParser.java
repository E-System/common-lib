package com.es.lib.common.number;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NumberParser {

    private final String value;

    public Short asShort(Short defValue) {
        return parse(defValue, Short::parseShort);
    }

    public Short asShort() {
        return asShort(null);
    }

    public Integer asInt(Integer defValue) {
        return parse(defValue, Integer::parseInt);
    }

    public Integer asInt() {
        return asInt(null);
    }

    public Long asLong(Long defValue) {
        return parse(defValue, Long::parseLong);
    }

    public Long asLong() {
        return asLong(null);
    }

    public Float asFloat(Float defValue) {
        return parse(defValue, Float::parseFloat);
    }

    public Float asFloat() {
        return asFloat(null);
    }

    public Double asDouble(Double defValue) {
        return parse(defValue, Double::parseDouble);
    }

    public Double asDouble() {
        return asDouble(null);
    }

    private <T> T parse(T defaultValue, Function<String, T> parser) {
        try {
            return parser.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
