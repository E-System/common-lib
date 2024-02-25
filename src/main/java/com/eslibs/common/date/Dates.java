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

package com.eslibs.common.date;

import com.eslibs.common.model.SItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dates {

    /**
     * Default timezone prefixes
     */
    public static final String DEFAULT_ZONES_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

    public static boolean isZoneValid(String id) {
        try {
            return ZoneId.of(id).getId().equals(id);
        } catch (Exception e) {
            return false;
        }
    }

    public static Collection<ZoneId> availableZones() {
        return ZoneId.getAvailableZoneIds().stream()
            .filter(v -> v.matches(DEFAULT_ZONES_PREFIXES))
            .map(ZoneId::of)
            .sorted(Comparator.comparing(ZoneId::getId))
            .collect(Collectors.toList());
    }

    public static boolean contains(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime date) {
        return date.isAfter(startDate) && !(endDate != null && !date.isBefore(endDate));
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
     * Get week number in year
     *
     * @param year       Year
     * @param month      Month
     * @param dayOfMonth Day of month
     * @return Week number in year
     */
    public static int getWeekNumber(int year, int month, int dayOfMonth) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return LocalDate.of(year, month, dayOfMonth).get(woy);
    }

    public static DateDiff diff(ChronoUnit unit) {
        return diff(unit, null);
    }

    public static DateDiff diff(ChronoUnit unit, ZoneId zoneId) {
        return new DateDiff(unit, zoneId);
    }

    public static PrettyInterval pretty() {
        return pretty(false);
    }

    public static PrettyInterval pretty(BiFunction<ChronoUnit, Long, String> localization) {
        return pretty(false, localization);
    }

    public static PrettyInterval pretty(boolean useBraces) {
        return pretty(useBraces, null);
    }

    public static PrettyInterval pretty(boolean useBraces, BiFunction<ChronoUnit, Long, String> localization) {
        return new PrettyInterval(useBraces, localization);
    }

    public static Collection<SItem> ranges(ZoneId zoneId, String pattern, boolean lastNextDay) {
        return Stream.of(DateRange.Interval.values())
            .map(v -> v.getItem(zoneId, pattern, lastNextDay))
            .collect(Collectors.toList());
    }

    public static Collection<SItem> ranges(ZoneId zoneId, boolean lastNextDay) {
        return Stream.of(DateRange.Interval.values())
            .map(v -> v.getItem(zoneId, lastNextDay))
            .collect(Collectors.toList());
    }

    public static Collection<SItem> ranges(ZoneId zoneId) {
        return ranges(zoneId, true);
    }

    public static DateRange defaultRange(ZoneId zoneId) {
        return defaultRange(zoneId, true);
    }

    public static DateRange defaultRange(ZoneId zoneId, boolean lastNextDay) {
        return DateRange.Interval.TODAY.getRange(zoneId, lastNextDay);
    }

    public static DateBuilder builder() {
        return builder(ZoneId.systemDefault());
    }

    public static DateBuilder builder(ZoneId zoneId) {
        return new DateBuilder(zoneId);
    }

    public static TimeConverter timeConverter() {
        return TimeConverter.getInstance();
    }

    public static DateFormatter formatter() {
        return formatter(null);
    }

    public static DateFormatter formatter(ZoneId zoneId) {
        return formatter(zoneId, null);
    }

    public static DateFormatter formatter(ZoneId zoneId, Locale locale) {
        return new DateFormatter(zoneId, locale);
    }

    public static DateParser parser() {
        return parser(null);
    }

    public static DateParser parser(ZoneId zoneId) {
        return parser(zoneId, null);
    }

    public static DateParser parser(ZoneId zoneId, Locale locale) {
        return new DateParser(zoneId, locale);
    }

    public static DateConverter converter() {
        return converter(ZoneId.systemDefault());
    }

    public static DateConverter converter(ZoneId zoneId) {
        return new DateConverter(zoneId);
    }

    public static DateGenerator generator() {
        return generator(ZoneId.systemDefault());
    }

    public static DateGenerator generator(ZoneId zoneId) {
        return new DateGenerator(zoneId);
    }
}
