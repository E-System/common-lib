package com.es.lib.common.time;

import com.es.lib.common.DateUtil;
import com.es.lib.common.Pluralizer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.05.2018
 */
public final class PeriodUtil {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    public static String pretty(Date date, boolean useBraces) {
        return pretty(DateUtil.convert(date), useBraces);
    }

    public static String pretty(LocalDateTime date, boolean useBraces) {
        return pretty(date, LocalDateTime.now(), useBraces);
    }

    public static String pretty(Date date, Date dateNext, boolean useBraces) {
        return pretty(DateUtil.convert(date), DateUtil.convert(dateNext), useBraces);
    }

    public static String pretty(LocalDateTime date, LocalDateTime dateNext, boolean useBraces) {
        if (date == null || dateNext == null) {
            return null;
        }
        long[] diff = getDiff(date, dateNext);
        Collection<String> parts = new ArrayList<>(6);
        if (diff[0] > 0) {
            parts.add(String.valueOf(diff[0]) + " " + Pluralizer.evaluate(diff[0], "год", "года", "лет"));
        }
        if (diff[1] > 0) {
            parts.add(String.valueOf(diff[1]) + " мес.");
        }
        if (diff[2] > 0) {
            parts.add(String.valueOf(diff[2]) + " дн.");
        }
        if (diff[3] > 0) {
            parts.add(String.valueOf(diff[3]) + " ч.");
        }
        if (diff[4] > 0) {
            parts.add(String.valueOf(diff[4]) + " м.");
        }
        if (diff[5] > 0) {
            parts.add(String.valueOf(diff[5]) + " c.");
        }
        String result = String.join(" ", parts);
        if (useBraces) {
            result = "(" + result + ")";
        }
        return result;
    }

    private static long[] getDiff(LocalDateTime date, LocalDateTime dateNext) {
        Period period = Period.between(date.toLocalDate(), dateNext.toLocalDate());
        long[] time = getTime(date, dateNext);
        return new long[]{
            period.getYears(),
            period.getMonths(),
            period.getDays(),
            time[0],
            time[1],
            time[2]
        };
    }

    private static long[] getTime(LocalDateTime date, LocalDateTime dateNext) {
        LocalDateTime original = LocalDateTime.of(dateNext.getYear(),
            dateNext.getMonthValue(), dateNext.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        Duration duration = Duration.between(original, dateNext);

        long seconds = duration.getSeconds();

        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        long secs = (seconds % SECONDS_PER_MINUTE);

        return new long[]{hours, minutes, secs};
    }
}
