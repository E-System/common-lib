package com.es.lib.common.time;

import com.es.lib.common.Constant;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 20.12.2017
 */
public final class TimeConverter {

    private TimeConverter() { }

    public static String asString(long value) {
        long hours = value / Constant.MULTIPLIER_HOUR;
        value %= Constant.MULTIPLIER_HOUR;
        long mins = value / Constant.MULTIPLIER_MINUTE;
        value %= Constant.MULTIPLIER_MINUTE;
        long secs = value / Constant.MULTIPLIER_MILLISECOND;
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
        result += hours * Constant.MULTIPLIER_HOUR;
        if (values.length > 1) {
            long min = Long.parseLong(values[1]);
            result += min * Constant.MULTIPLIER_MINUTE;
        }
        if (values.length > 2) {
            long sec = Long.parseLong(values[2]);
            result += sec * Constant.MULTIPLIER_MILLISECOND;
        }
        return result;
    }
}
