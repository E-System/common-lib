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
package com.eslibs.common.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 20.12.2017
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConverter {

    public String toString(long value) {
        Duration duration = Duration.ofMillis(value);
        int secs = duration.toSecondsPart();
        return String.format("%02d", duration.toHoursPart()) + ":" +
               String.format("%02d", duration.toMinutesPart()) +
               (secs == 0 ? "" : (":" + String.format("%02d", secs)));
    }

    public Long toLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String[] values = value.split(":");
        if (values.length == 0) {
            return null;
        }
        Duration duration = Duration.ofHours(parse(values[0]));
        if (values.length > 1) {
            duration = duration.plusMinutes(parse(values[1]));
        }
        if (values.length > 2) {
            duration = duration.plusSeconds(parse(values[2]));
        }
        return duration.toMillis();
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
