package com.es.lib.common.date;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public Map<Integer, Map<Integer, Collection<LocalDate>>> calendar(LocalDate startYear, int count) {
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

    public Collection<LocalDate> days(LocalDate fromInclusive, LocalDate toExclusive, Predicate<LocalDate> filter) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(fromInclusive, toExclusive);
        return IntStream.iterate(0, v -> v + 1)
                        .limit(numOfDaysBetween)
                        .mapToObj(fromInclusive::plusDays)
                        .filter(v -> filter == null || filter.test(v))
                        .collect(Collectors.toList());
    }
}
