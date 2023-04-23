package com.eslibs.common.date;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
public class DateConverter {

    private final ZoneId zoneId;

    /**
     * Convert Date to LocalDateTime
     *
     * @param date Date
     * @return Local date
     */
    public LocalDateTime get(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Convert LocalDateTime to Date
     *
     * @param date Local date
     * @return Date
     */
    public Date get(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.atZone(zoneId).toInstant();
        return Date.from(instant);
    }
}
