package com.es.lib.common.time;

import com.es.lib.common.DateUtil;
import com.es.lib.common.Pluralizer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
        Period period = Period.between(date.toLocalDate(), dateNext.toLocalDate());
        long[] time = getTime(date, dateNext);
        StringBuilder sb = new StringBuilder();
        if (period.getYears() > 0) {
            sb.append(period.getYears()).append(" ").append(Pluralizer.evaluate(period.getYears(), "год", "года", "лет"));
        }
        if (period.getMonths() > 0) {
            sb.append(period.getMonths()).append(" мес.");
        }
        if (period.getDays() > 0) {
            sb.append(period.getDays()).append(" дн.");
        }
        if (time[0] > 0) {
            sb.append(time[0]).append(" ч.");
        }
        if (time[1] > 0) {
            sb.append(time[1]).append(" м.");
        }
        if (time[2] > 0) {
            sb.append(time[2]).append(" c.");
        }
        String result = sb.toString();
        if (useBraces) {
            result = "(" + result + ")";
        }
        return result;
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
