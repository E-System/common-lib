package com.es.lib.common.date;

import com.es.lib.common.Constant;
import com.es.lib.common.text.TextUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
    private final BiFunction<DurationType, Long, String> localization;

    PrettyInterval(boolean useBraces, BiFunction<DurationType, Long, String> localization) {
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

    private static Collection<Map.Entry<DurationType, Long>> getDiff(LocalDateTime date, LocalDateTime dateNext) {
        Period period = Period.between(date.toLocalDate(), dateNext.toLocalDate());
        long[] time = getTime(date, dateNext);
        return Arrays.asList(
            Pair.of(DurationType.YEAR, (long) period.getYears()),
            Pair.of(DurationType.MONTH, (long) period.getMonths()),
            Pair.of(DurationType.DAY, (long) period.getDays()),
            Pair.of(DurationType.HOUR, time[0]),
            Pair.of(DurationType.MINUTE, time[1]),
            Pair.of(DurationType.SECOND, time[2])
        );
    }

    private static long[] getTime(LocalDateTime date, LocalDateTime dateNext) {
        LocalDateTime original = LocalDateTime.of(dateNext.getYear(),
            dateNext.getMonthValue(), dateNext.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        Duration duration = Duration.between(original, dateNext);

        long seconds = duration.getSeconds();

        long hours = seconds / Constant.SECONDS_IN_HOUR;
        long minutes = ((seconds % Constant.SECONDS_IN_HOUR) / Constant.SECONDS_IN_MINUTE);
        long secs = (seconds % Constant.SECONDS_IN_MINUTE);

        return new long[]{hours, minutes, secs};
    }

    public enum DurationType {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE,
        SECOND
    }

    public static BiFunction<DurationType, Long, String> DEFAULT_LOCALIZATION = (type, value) -> {
        switch (type) {
            case YEAR:
                return value + " " + TextUtil.pluralize(value, "год", "года", "лет");
            case MONTH:
                return value + " мес.";
            case DAY:
                return value + " дн.";
            case HOUR:
                return value + " ч.";
            case MINUTE:
                return value + " м.";
            case SECOND:
                return value + " c.";
            default:
                return "";
        }
    };
}
