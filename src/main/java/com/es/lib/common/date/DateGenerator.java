package com.es.lib.common.date;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RequiredArgsConstructor
public class DateGenerator {

    private final ZoneId zoneId;

    public Date nextDay(Date date) {
        return Date.from(
            ZonedDateTime.ofInstant(date.toInstant(), zoneId).plusDays(1).toInstant()
        );
    }

    /**
     * Get start of month
     *
     * @return Start of month
     */
    public Date monthStart() {
        return Date.from(
            LocalDate.now().withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
        );
    }

    public Date yesterday() {
        return yesterday(null);
    }

    public Date yesterday(Integer hour) {
        return new DateBuilder(zoneId).addDayOfMonth(-1).clearTime().setHourOfDay(hour).build();
    }

    public Date today() {
        return today(null);
    }

    public Date today(Integer hour) {
        return new DateBuilder(zoneId).clearTime().setHourOfDay(hour).build();
    }

    public Date tomorrow() {
        return tomorrow(null);
    }

    public Date tomorrow(Integer hour) {
        return new DateBuilder(zoneId).addDayOfMonth(1).clearTime().setHourOfDay(hour).build();
    }

    public Date now() {
        return new DateBuilder(zoneId).build();
    }

    public Date nowOffset(int field, int amount) {
        return new DateBuilder(zoneId).add(field, amount).build();
    }
}
