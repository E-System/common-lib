/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class DateUtil {

    public static final String CALENDAR_DATE_PATTERN = "dd.MM.yyyy";

    private DateUtil() {}

    public static Date nextDay(Date date, TimeZone timeZone) {
        return Date.from(
            ZonedDateTime.ofInstant(
                date.toInstant(),
                timeZone.toZoneId()
            ).plusDays(1).toInstant()
        );
    }

    public static boolean contains(Date dbegin, Date dend, Date date, TimeZone timeZone) {
        LocalDateTime start = LocalDateTime.ofInstant(dbegin.toInstant(), timeZone.toZoneId());
        LocalDateTime end = dend != null ? LocalDateTime.ofInstant(dend.toInstant(), timeZone.toZoneId()) : null;
        LocalDateTime dt = LocalDateTime.ofInstant(date.toInstant(), timeZone.toZoneId());
        return dt.isAfter(start) && !(end != null && !dt.isBefore(end));
    }

    /**
     * Получить количество юнитов между переданной датой и текущей
     *
     * @param chronoUnit тип юнитов (дни, часы....)
     * @param start      начало интервала
     * @return количество юнитов между переданной датой и текущей
     */
    public static long between(ChronoUnit chronoUnit, Date start) {
        return chronoUnit.between(
            start.toInstant(),
            ZonedDateTime.now()
        );
    }

    /**
     * Получить количество юнитов между переданными датами
     *
     * @param chronoUnit тип юнитов (дни, часы....)
     * @param start      начало интервала
     * @param end        конец интервала
     * @return количество юнитов между переданными датами
     */
    public static long between(ChronoUnit chronoUnit, Date start, Date end) {
        return chronoUnit.between(
            start.toInstant(),
            end.toInstant()
        );
    }

    /**
     * Проверить что дата не принадлежит сегодня (меньше начала сегодняшнего дня)
     *
     * @param dateTime дата для проверки
     * @return true - дата не принадлежит сегодня (меньше начала сегодняшнего дня)
     */
    public static boolean isBeforeToday(LocalDateTime dateTime) {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).isAfter(dateTime);
    }

    /**
     * Проверить что дата в будущем (более текущей даты)
     *
     * @param date     дата для проверки
     * @param timeZone таймзона
     * @return true - дата в будущем (более текущей даты)
     */
    public static boolean isAfterNow(Date date, TimeZone timeZone) {
        LocalDateTime dt = LocalDateTime.ofInstant(date.toInstant(), timeZone.toZoneId());
        LocalDateTime now = LocalDateTime.now(timeZone.toZoneId());
        return dt.isAfter(now);
    }

    /**
     * Get start of month
     *
     * @return Start of month
     */
    public static Date monthStart() {
        return monthStart(ZoneId.systemDefault());
    }

    /**
     * Get start of month
     *
     * @param zoneId Timezone
     * @return Start of month
     */
    public static Date monthStart(ZoneId zoneId) {
        return Date.from(
            LocalDate.now().withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
        );
    }

    /**
     * Get today begin
     *
     * @return Today begin
     */
    public static Date todayBegin() {
        return Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Calendar clearTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Date yesterday(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return clearTime(cal).getTime();
    }

    public static Date today(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        return clearTime(cal).getTime();
    }

    public static Date tommorow(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return clearTime(cal).getTime();
    }

    public static Date todayWithHour(TimeZone timeZone, Integer hour) {
        Calendar cal = Calendar.getInstance(timeZone);
        if (hour != null) {
            cal.set(Calendar.HOUR_OF_DAY, hour);
        }
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date now(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    public static Date nowOffset(TimeZone timeZone, int field, int amount) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static String getYearIndex(Date date) {
        return format(date, "yy");
    }

    public static String format(Date date, String format) {
        return createDateFormat(format).format(date);
    }

    public static String format(Date date) {
        return format(date, CALENDAR_DATE_PATTERN);
    }

    public static String format(TimeZone timeZone, Date date, String format) {
        return createDateFormat(format, timeZone).format(date);
    }

    public static String format(TimeZone timeZone, Date date) {
        return format(timeZone, date, CALENDAR_DATE_PATTERN);
    }

    public static String format(Locale locale, Date date, String format) {
        return createDateFormat(format, locale).format(date);
    }

    public static String format(Locale locale, Date date) {
        return format(locale, date, CALENDAR_DATE_PATTERN);
    }

    public static Date parse(String date, String format) throws ParseException {
        return createDateFormat(format).parse(date);
    }

    private static SimpleDateFormat createDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    private static SimpleDateFormat createDateFormat(String format, TimeZone timeZone) {
        SimpleDateFormat result = createDateFormat(format);
        result.setTimeZone(timeZone);
        return result;
    }

    private static SimpleDateFormat createDateFormat(String format, Locale locale) {
        return new SimpleDateFormat(format, locale);
    }

    public static Calendar getCalendar(Date date, TimeZone timeZone) {
        Calendar cal = getCalendar(timeZone);
        cal.setTime(date);
        return cal;
    }

    public static Calendar getCalendar(TimeZone timeZone) {
        return Calendar.getInstance(timeZone);
    }

    /**
     * Get week day
     *
     * @param year       Year
     * @param month      Month
     * @param dayOfMonth Day of month
     * @return Week day
     */
    public static DayOfWeek getWeekDay(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth).getDayOfWeek();
    }

    /**
     * Get week number in year
     *
     * @param year       Year
     * @param month      Month
     * @param dayOfMonth Day of month
     * @return Week number in year
     */
    public static int getWeekNumber(int year, int month, int dayOfMonth) {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return date.get(woy);
    }

    /**
     * Convert Date to LocalDateTime
     *
     * @param date   Date
     * @param zoneId Timezone
     * @return Local date
     */
    public static LocalDateTime convert(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * Convert Date to LocalDateTime
     *
     * @param date Date
     * @return Local Date
     */
    public static LocalDateTime convert(Date date) {
        return convert(date, ZoneId.systemDefault());
    }

    /**
     * Convert LocalDateTime to Date
     *
     * @param date   Local date
     * @param zoneId Timezone
     * @return Date
     */
    public static Date convert(LocalDateTime date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        Instant instant = date.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * Convert LocalDateTime to Date
     *
     * @param date Local date
     * @return Date
     */
    public static Date convert(LocalDateTime date) {
        return convert(date, ZoneId.systemDefault());
    }
}
