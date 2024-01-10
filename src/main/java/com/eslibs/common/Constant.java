/*
 * Copyright 2016 E-System LLC
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

package com.eslibs.common;

import com.eslibs.common.text.Texts;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Base constants
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public interface Constant {

    BiFunction<ChronoUnit, Long, String> DEFAULT_LOCALIZATION = (type, value) -> switch (type) {
        case YEARS -> value + " " + Texts.pluralize(value, "год", "года", "лет");
        case MONTHS -> value + " мес.";
        case DAYS -> value + " дн.";
        case HOURS -> value + " ч.";
        case MINUTES -> value + " м.";
        case SECONDS -> value + " c.";
        default -> throw new IllegalStateException("Unexpected value: " + type);
    };

    long DEFAULT_CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(20);

    long DEFAULT_RW_TIMEOUT = TimeUnit.SECONDS.toMillis(60);

    /**
     * Default grouping size (Example 1 000 000.12)
     */
    int DEFAULT_GROUPING_SIZE = 3;
    /**
     * Default decimal count (Example 10.12)
     */
    int DEFAULT_DECIMAL_COUNT = 2;
    int DEFAULT_SCALE = 6;
    int PRECISED_SCALE = 7;

    long SCALED_LONG_MULTIPLIER = 1000000;
    /**
     * Default timezone prefixes
     */
    String DEFAULT_ZONES_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";
    /**
     * Default date pattern
     */
    String DEFAULT_DATE_PATTERN = "dd.MM.yyyy";
    /**
     * Default date pattern with time
     */
    String DEFAULT_DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";

    /**
     * Sortable date pattern with time
     */
    String SORTABLE_DATE_TIME_PATTERN = "yyyy.MM.dd HH:mm:ss";
    /**
     * Sortable date pattern
     */
    String SORTABLE_DATE_PATTERN = "yyyy.MM.dd";

    /**
     * Default encoding
     */
    Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    static byte[] bytes(String value) {
        return Objects.requireNonNull(value, "value must not be null")
            .getBytes(DEFAULT_ENCODING);
    }

    /**
     * Default autocomplete size
     */
    int AUTO_COMPLETE_SIZE = 20;
    /**
     * Min percent value
     */
    double PERCENT_MIN = 0.0d;
    /**
     * Max percent value
     */
    double PERCENT_MAX = 100.0d;
    /**
     * Max days count in month
     */
    int MAX_MONTH_DAYS = 31;

    int SECONDS_IN_MINUTE = 60;
    int MINUTES_IN_HOUR = 60;
    int HOURS_IN_DAY = 24;
    int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
    int MINUTES_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR;
    int SECONDS_IN_DAY = MINUTES_IN_DAY * SECONDS_IN_HOUR;

    long MILLIS_IN_SECOND = 1000;
    long MILLIS_IN_MINUTE = SECONDS_IN_MINUTE * MILLIS_IN_SECOND;
    long MILLIS_IN_HOUR = MINUTES_IN_HOUR * MILLIS_IN_MINUTE;

    Collection<String> WEEK_FULL = List.of("1", "2", "3", "4", "5", "6", "7");
    Collection<String> WEEK_WORK = List.of("1", "2", "3", "4", "5");
    Collection<String> WEEK_ENDS = List.of("6", "7");
}
