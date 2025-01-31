package com.eslibs.common.collection;

import com.eslibs.common.date.Dates;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SafeMap extends HashMap<String, String> {

    SafeMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public Optional<Short> getShort(String key) {
        return parse(key, Short::parseShort);
    }

    public Optional<Integer> getInt(String key) {
        return parse(key, Integer::parseInt);
    }

    public Optional<Long> getLong(String key) {
        return parse(key, Long::parseLong);
    }

    public Optional<Double> getDouble(String key) {
        return parse(key, Double::parseDouble);
    }

    public Optional<TemporalAccessor> getDate(String key) {
        return getDate(key, null);
    }

    public Optional<TemporalAccessor> getDate(String key, DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            dateTimeFormatter = Dates.getEnvironment().getDateFormatter();
        }
        try {
            return Optional.of(dateTimeFormatter.parse(get(key)));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    private <T> Optional<T> parse(String key, Function<String, T> parser) {
        try {
            return Optional.of(parser.apply(get(key)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}