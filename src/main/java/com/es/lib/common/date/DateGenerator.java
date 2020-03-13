package com.es.lib.common.date;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    /**
     * Get today begin
     *
     * @return Today begin
     */
    public Date todayBegin() {
        return Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(zoneId).toInstant());
    }

    public Date yesterday() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return clearTime(calendar).getTime();
    }

    public Date today() {
        return today(null);
    }

    public Date today(Integer hour) {
        Calendar calendar = getCalendar();
        clearTime(calendar);
        if (hour != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        return calendar.getTime();
    }

    public Date tomorrow() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return clearTime(calendar).getTime();
    }

    public Date now() {
        return getCalendar().getTime();
    }

    public Date nowOffset(int field, int amount) {
        Calendar calendar = getCalendar();
        calendar.add(field, amount);
        return calendar.getTime();
    }

    private Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(zoneId));
    }

    private Calendar getCalendar(Date date) {
        Calendar result = getCalendar();
        result.setTime(date);
        return result;
    }

    private Calendar clearTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
