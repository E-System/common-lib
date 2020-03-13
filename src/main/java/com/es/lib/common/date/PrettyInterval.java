package com.es.lib.common.date;

import com.es.lib.common.Constant;
import com.es.lib.common.text.TextUtil;

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
public final class PrettyInterval {

    private final boolean useBraces;

    PrettyInterval(boolean useBraces) {
        this.useBraces = useBraces;
    }

    public String get(Date date) {
        return get(DateUtil.converter().get(date));
    }

    public String get(LocalDateTime date) {
        return get(date, LocalDateTime.now());
    }

    public String get(Date date, Date dateNext) {
        DateConverter converter = DateUtil.converter();
        return get(converter.get(date), converter.get(dateNext));
    }

    public String get(LocalDateTime date, LocalDateTime dateNext) {
        if (date == null || dateNext == null) {
            return null;
        }
        long[] diff = getDiff(date, dateNext);
        Collection<String> parts = new ArrayList<>(6);
        if (diff[0] > 0) {
            parts.add(diff[0] + " " + TextUtil.pluralize(diff[0], "год", "года", "лет"));
        }
        if (diff[1] > 0) {
            parts.add(diff[1] + " мес.");
        }
        if (diff[2] > 0) {
            parts.add(diff[2] + " дн.");
        }
        if (diff[3] > 0) {
            parts.add(diff[3] + " ч.");
        }
        if (diff[4] > 0) {
            parts.add(diff[4] + " м.");
        }
        if (diff[5] > 0) {
            parts.add(diff[5] + " c.");
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

        long hours = seconds / Constant.SECONDS_IN_HOUR;
        long minutes = ((seconds % Constant.SECONDS_IN_HOUR) / Constant.SECONDS_IN_MINUTE);
        long secs = (seconds % Constant.SECONDS_IN_MINUTE);

        return new long[]{hours, minutes, secs};
    }
}
