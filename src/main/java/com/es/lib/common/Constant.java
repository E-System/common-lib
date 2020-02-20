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

package com.es.lib.common;

/**
 * Base constants
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public interface Constant {

    /**
     * Default encoding
     */
    String DEFAULT_ENCODING = "utf8";
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
}
