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

    private static String NON_NUMBER_PATTERN = "[^0-9]";

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
        if (values.length == 0) {
            return null;
        }
        long hours = parse(values[0]);
        result += hours * MULTIPLIER_HOUR;
        if (values.length > 1) {
            long min = parse(values[1]);
            result += min * MULTIPLIER_MINUTE;
        }
        if (values.length > 2) {
            long sec = parse(values[2]);
            result += sec * MULTIPLIER_MILLISECOND;
        }
        return result;
    }

    private static long parse(String value) {
        if (value == null) {
            return 0;
        }
        value = value.replaceAll(NON_NUMBER_PATTERN, "");
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }
}
