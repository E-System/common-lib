package com.eslibs.common.date;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DateParser {

    private final Dates.Environment environment;
    private final String value;

    public Optional<Instant> asInstant() {
        return asInstant(null);
    }

    public Optional<Instant> asInstant(DateTimeFormatter dateTimeFormatter) {
        return parse(dateTimeFormatter, (dtf, v) -> dtf.parse(v, Instant::from));
    }

    private <T> Optional<T> parse(DateTimeFormatter dateTimeFormatter, BiFunction<DateTimeFormatter, String, T> parser) {
        dateTimeFormatter = dateTimeFormatter != null ? dateTimeFormatter : environment.getDateTimeFormatter();
        try {
            return Optional.of(parser.apply(dateTimeFormatter, value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
