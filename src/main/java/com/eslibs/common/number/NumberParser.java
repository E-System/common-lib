package com.eslibs.common.number;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NumberParser {

    private final String value;

    public Optional<Short> asShort() {
        return parse(Short::parseShort);
    }

    public Optional<Integer> asInt() {
        return parse(Integer::parseInt);
    }

    public Optional<Long> asLong() {
        return parse(Long::parseLong);
    }

    public Optional<Float> asFloat() {
        return parse(Float::parseFloat);
    }

    public Optional<Double> asDouble() {
        return parse(Double::parseDouble);
    }

    public Optional<Long> asSum() {
        return parse(v -> Math.round(Double.parseDouble(v.replace(",", ".")) * 100.0));
    }

    private <T> Optional<T> parse(Function<String, T> parser) {
        try {
            return Optional.of(parser.apply(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
