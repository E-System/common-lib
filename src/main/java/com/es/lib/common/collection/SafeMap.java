package com.es.lib.common.collection;

import com.es.lib.common.Constant;
import com.es.lib.common.date.Dates;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SafeMap extends HashMap<String, String> {

    public SafeMap(Map<? extends String, ? extends String> m) {
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

    public Optional<Date> getDate(String key) {
        return getDate(key, Constant.DEFAULT_DATE_PATTERN);
    }

    public Optional<Date> getDate(String key, String pattern) {
        try {
            return Optional.ofNullable(Dates.parser().parse(get(key), pattern));
        } catch (ParseException e) {
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