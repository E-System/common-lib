package com.eslibs.common.date;

import com.eslibs.common.Constant;
import com.eslibs.common.text.Texts;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.05.2018
 */
public final class PrettyInterval {

    private final boolean useBraces;
    private final BiFunction<ChronoUnit, Long, String> localization;

    PrettyInterval(boolean useBraces, BiFunction<ChronoUnit, Long, String> localization) {
        this.useBraces = useBraces;
        this.localization = localization != null ? localization : DEFAULT_LOCALIZATION;
    }

    public String get(Date date) {
        return get(Dates.converter().get(date));
    }

    public String get(LocalDateTime date) {
        return get(date, LocalDateTime.now());
    }

    public String get(Date date, Date dateNext) {
        DateConverter converter = Dates.converter();
        return get(converter.get(date), converter.get(dateNext));
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
        Map.Entry<long[], Boolean> t = getTime(date, dateNext);
        long[] time = t.getKey();
        boolean shift = t.getValue();
        Period period = Period.between(date.toLocalDate(), dateNext.toLocalDate());
        return Arrays.asList(
            Pair.of(ChronoUnit.YEARS, (long) period.getYears()),
            Pair.of(ChronoUnit.MONTHS, (long) period.getMonths()),
            Pair.of(ChronoUnit.DAYS, (long) period.getDays() - (shift ? 1 : 0)),
            Pair.of(ChronoUnit.HOURS, time[0]),
            Pair.of(ChronoUnit.MINUTES, time[1]),
            Pair.of(ChronoUnit.SECONDS, time[2])
        );
    }

    private static Map.Entry<long[], Boolean> getTime(LocalDateTime date, LocalDateTime dateNext) {
        LocalDateTime original = LocalDateTime.of(dateNext.getYear(),
                                                  dateNext.getMonthValue(), dateNext.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        Duration duration = Duration.between(original, dateNext);
        boolean shift = false;
        long seconds = duration.getSeconds();
        if (seconds < 0) {
            seconds += 24 * Constant.SECONDS_IN_HOUR;
            shift = true;
        }

        long hours = seconds / Constant.SECONDS_IN_HOUR;
        long minutes = ((seconds % Constant.SECONDS_IN_HOUR) / Constant.SECONDS_IN_MINUTE);
        long secs = (seconds % Constant.SECONDS_IN_MINUTE);

        return Pair.of(new long[]{hours, minutes, secs}, shift);
    }

    public static BiFunction<ChronoUnit, Long, String> DEFAULT_LOCALIZATION = (type, value) -> switch (type) {
        case YEARS -> value + " " + Texts.pluralize(value, "год", "года", "лет");
        case MONTHS -> value + " мес.";
        case DAYS -> value + " дн.";
        case HOURS -> value + " ч.";
        case MINUTES -> value + " м.";
        case SECONDS -> value + " c.";
        default -> throw new IllegalStateException("Unexpected value: " + type);
    };
}
