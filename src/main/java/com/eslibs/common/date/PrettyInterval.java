package com.eslibs.common.date;

import com.eslibs.common.text.Texts;
import org.apache.commons.lang3.tuple.Pair;

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
public final class PrettyInterval {

    BiFunction<ChronoUnit, Long, String> DEFAULT_LOCALIZATION = (type, value) -> switch (type) {
        case YEARS -> value + " " + Texts.pluralize(value, "год", "года", "лет");
        case MONTHS -> value + " мес.";
        case DAYS -> value + " дн.";
        case HOURS -> value + " ч.";
        case MINUTES -> value + " м.";
        case SECONDS -> value + " c.";
        default -> throw new IllegalStateException("Unexpected value: " + type);
    };

    private final boolean useBraces;
    private final BiFunction<ChronoUnit, Long, String> localization;

    PrettyInterval(boolean useBraces, BiFunction<ChronoUnit, Long, String> localization) {
        this.useBraces = useBraces;
        this.localization = localization != null ? localization : DEFAULT_LOCALIZATION;
    }

    public String get(LocalDateTime date) {
        return get(date, LocalDateTime.now());
    }

    public String get(LocalDateTime date, LocalDateTime dateNext) {
        if (date == null || dateNext == null) {
            return null;
        }
        String result = getDiff(date, dateNext)
            .stream()
            .filter(v -> v.getValue() > 0)
            .map(v -> localization.apply(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));
        if (useBraces) {
            result = "(" + result + ")";
        }
        return result;
    }

    private static Collection<Map.Entry<ChronoUnit, Long>> getDiff(LocalDateTime date, LocalDateTime dateNext) {
        Period period = Period.between(date.toLocalDate(), dateNext.toLocalDate());
        Duration duration = Duration.between(date.toLocalTime(), dateNext.toLocalTime());
        return Arrays.asList(
            Pair.of(ChronoUnit.YEARS, (long) period.getYears()),
            Pair.of(ChronoUnit.MONTHS, (long) period.getMonths()),
            Pair.of(ChronoUnit.DAYS, (long) period.getDays()),
            Pair.of(ChronoUnit.HOURS, (long) duration.toHoursPart()),
            Pair.of(ChronoUnit.MINUTES, (long) duration.toMinutesPart()),
            Pair.of(ChronoUnit.SECONDS, (long) duration.toSecondsPart())
        );
    }
}
