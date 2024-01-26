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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.MonthDay;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base constants
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public interface Constant {

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
     * Max days count in month
     */
    int MAX_MONTH_DAYS = 31;

    Collection<String> WEEK_FULL = List.of("1", "2", "3", "4", "5", "6", "7");
    Collection<String> WEEK_WORK = List.of("1", "2", "3", "4", "5");
    Collection<String> WEEK_ENDS = List.of("6", "7");
}
