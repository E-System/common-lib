package com.eslibs.common.date;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.05.2018
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class PrettyInterval {

    private final boolean useBraces;
    private final BiFunction<ChronoUnit, Long, String> localization;

    public String get(LocalDateTime from) {
        return get(from, LocalDateTime.now());
    }

    public String get(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            return null;
        }
        String result = getDiff(from, to)
            .stream()
            .filter(v -> v.getValue() > 0)
            .map(v -> localization.apply(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));
        if (useBraces) {
            result = "(" + result + ")";
        }
        return result;
    }

    private static Collection<Map.Entry<ChronoUnit, Long>> getDiff(LocalDateTime from, LocalDateTime to) {
        Period period = Period.between(from.toLocalDate(), to.toLocalDate());
        Duration duration = Duration.between(from.toLocalTime(), to.toLocalTime());
        int days = period.getDays();
        if (duration.isNegative()) {
            duration = duration.plusDays(1);
            days -= 1;
        }
        return Arrays.asList(
            Map.entry(ChronoUnit.YEARS, (long) period.getYears()),
            Map.entry(ChronoUnit.MONTHS, (long) period.getMonths()),
            Map.entry(ChronoUnit.DAYS, (long) days),
            Map.entry(ChronoUnit.HOURS, (long) duration.toHoursPart()),
            Map.entry(ChronoUnit.MINUTES, (long) duration.toMinutesPart()),
            Map.entry(ChronoUnit.SECONDS, (long) duration.toSecondsPart())
        );
    }
}
