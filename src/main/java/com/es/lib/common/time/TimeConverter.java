package com.es.lib.common.time;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 20.12.2017
 */
public final class TimeConverter {

    public static final long MULTIPLIER_MILLISECOND = 1000;
    public static final long MULTIPLIER_MINUTE = 60 * MULTIPLIER_MILLISECOND;
    public static final long MULTIPLIER_HOUR = 60 * MULTIPLIER_MINUTE;

    private TimeConverter() { }

    public static String asString(long value) {
        long hours = value / MULTIPLIER_HOUR;
        value %= MULTIPLIER_HOUR;
        long mins = value / MULTIPLIER_MINUTE;
        value %= MULTIPLIER_MINUTE;
        long secs = value / MULTIPLIER_MILLISECOND;
        return String.format("%02d", hours) + ":" +
               String.format("%02d", mins) +
               (secs == 0 ? "" : (":" + String.format("%02d", secs)));
    }

    public static Long asLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        long result = 0;
        String[] values = value.split(":");
        long hours = Long.parseLong(values[0]);
        result += hours * MULTIPLIER_HOUR;
        if (values.length > 1) {
            long min = Long.parseLong(values[1]);
            result += min * MULTIPLIER_MINUTE;
        }
        if (values.length > 2) {
            long sec = Long.parseLong(values[2]);
            result += sec * MULTIPLIER_MILLISECOND;
        }
        return result;
    }
}
