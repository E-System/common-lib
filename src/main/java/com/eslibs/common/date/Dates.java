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
import com.eslibs.common.text.Texts;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Dates {

    @Builder
    @Getter
    @RequiredArgsConstructor
    public static class Environment {

        @Builder.Default
        private final ZoneId zoneId = ZoneId.systemDefault();
        @Builder.Default
        private final Locale locale = Locale.getDefault();
        @Builder.Default
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        @Builder.Default
        private final DateTimeFormatter sortableDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        @Builder.Default
        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        @Builder.Default
        private final DateTimeFormatter sortableDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        @Builder.Default
        private final String zonePrefixes = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";
        @Builder.Default
        private final BiFunction<ChronoUnit, Long, String> intervalLocalization = (type, value) -> switch (type) {
            case YEARS -> value + " " + Texts.pluralize(value, "год", "года", "лет");
            case MONTHS -> value + " мес.";
            case DAYS -> value + " дн.";
            case HOURS -> value + " ч.";
            case MINUTES -> value + " м.";
            case SECONDS -> value + " c.";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Getter
    @Setter
    private static Environment environment = Environment.builder().build();

    public static boolean isZoneValid(String id) {
        try {
            return ZoneId.of(id).getId().equals(id);
        } catch (Exception e) {
            return false;
        }
    }

    public static Collection<ZoneId> availableZones() {
        return ZoneId.getAvailableZoneIds().stream()
            .filter(v -> v.matches(getEnvironment().zonePrefixes))
            .map(ZoneId::of)
            .sorted(Comparator.comparing(ZoneId::getId))
            .toList();
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
        return new PrettyInterval(useBraces, localization != null ? localization : getEnvironment().intervalLocalization);
    }

    public static Collection<SItem> ranges(ZoneId zoneId, DateTimeFormatter dateTimeFormatter, boolean lastNextDay) {
        return Stream.of(DateRange.Interval.values())
            .map(v -> v.getItem(zoneId, dateTimeFormatter, lastNextDay))
            .toList();
    }

    public static Collection<SItem> ranges(ZoneId zoneId, boolean lastNextDay) {
        return Stream.of(DateRange.Interval.values())
            .map(v -> v.getItem(zoneId, lastNextDay))
            .toList();
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

    public static TimeConverter timeConverter() {
        return TimeConverter.getInstance();
    }


    public static DateConverter converter() {
        return converter(ZoneId.systemDefault());
    }

    public static DateConverter converter(ZoneId zoneId) {
        return new DateConverter(zoneId);
    }

    public static Map<Integer, Map<Integer, Collection<LocalDate>>> calendar(LocalDate startYear, int count) {
        Map<Integer, Map<Integer, Collection<LocalDate>>> result = new LinkedHashMap<>();
        long numOfDaysBetween = ChronoUnit.DAYS.between(startYear, startYear.plusYears(count));
        IntStream.iterate(0, v -> v + 1)
            .limit(numOfDaysBetween)
            .mapToObj(startYear::plusDays)
            .forEach(v ->
                         result.computeIfAbsent(v.getYear(), y -> new LinkedHashMap<>()).computeIfAbsent(v.getMonthValue(), m -> new ArrayList<>()).add(v)
            );
        return result;
    }

    public static Collection<LocalDate> days(LocalDate fromInclusive, LocalDate toExclusive, Predicate<LocalDate> filter) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(fromInclusive, toExclusive);
        return IntStream.iterate(0, v -> v + 1)
            .limit(numOfDaysBetween)
            .mapToObj(fromInclusive::plusDays)
            .filter(v -> filter == null || filter.test(v))
            .toList();
    }
}
