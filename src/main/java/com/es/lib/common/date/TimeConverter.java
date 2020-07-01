/*
 * Copyright 2020 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.es.lib.common.date;

import com.es.lib.common.Constant;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 20.12.2017
 */
public final class TimeConverter {

    private TimeConverter() { }

    public String toString(long value) {
        long hours = value / Constant.MILLIS_IN_HOUR;
        value %= Constant.MILLIS_IN_HOUR;
        long mins = value / Constant.MILLIS_IN_MINUTE;
        value %= Constant.MILLIS_IN_MINUTE;
        long secs = value / Constant.MILLIS_IN_SECOND;
        return String.format("%02d", hours) + ":" +
               String.format("%02d", mins) +
               (secs == 0 ? "" : (":" + String.format("%02d", secs)));
    }

    public Long toLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        long result = 0;
        String[] values = value.split(":");
        if (values.length == 0) {
            return null;
        }
        long hours = parse(values[0]);
        result += hours * Constant.MILLIS_IN_HOUR;
        if (values.length > 1) {
            long min = parse(values[1]);
            result += min * Constant.MILLIS_IN_MINUTE;
        }
        if (values.length > 2) {
            long sec = parse(values[2]);
            result += sec * Constant.MILLIS_IN_SECOND;
        }
        return result;
    }

    private long parse(String value) {
        if (value == null) {
            return 0;
        }
        value = value.replaceAll("[^0-9]", "");
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }
    
    public static TimeConverter getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static TimeConverter INSTANCE = new TimeConverter();
    }
}
