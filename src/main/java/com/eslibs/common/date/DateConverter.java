package com.eslibs.common.date;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
public class DateConverter {

    private final ZoneId zoneId;

    /**
     * Convert Date to LocalDateTime
     *
     * @param date Date
     * @return Local date
     */
    public Optional<LocalDateTime> asLocalDateTime(Date date) {
        if (date == null) {
            return Optional.empty();
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return Optional.of(LocalDateTime.ofInstant(instant, zoneId));
    }

    /**
     * Convert LocalDateTime to Date
     *
     * @param date Local date
     * @return Date
     */
    public Optional<Date> asDate(LocalDateTime date) {
        if (date == null) {
            return Optional.empty();
        }
        Instant instant = date.atZone(zoneId).toInstant();
        return Optional.of(Date.from(instant));
    }
}
