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

package com.es.lib.common

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
class PercentUtilSpec extends Specification {

    def "Get int percent value"() {
        expect:
        PercentUtil.get(value1 as int, value2 as double) == result as int
        where:
        value1 | value2 || result
        0      | 0.0d   || 0
        0      | 1.0d   || 0
        1      | 10.0d  || 0
        4      | 10.0d  || 0
        5      | 10.0d  || 1
        10     | 10.0d  || 1
        14     | 10.0d  || 1
        15     | 10.0d  || 2
        20     | 10.0d  || 2
    }

    def "Get long percent value"() {
        expect:
        PercentUtil.get(value1 as long, value2 as double) == result as long
        where:
        value1 | value2 || result
        0L     | 0.0d   || 0L
        0L     | 1.0d   || 0L
        1L     | 10.0d  || 0L
        4L     | 10.0d  || 0L
        5L     | 10.0d  || 1L
        10L    | 10.0d  || 1L
        14L    | 10.0d  || 1L
        15L    | 10.0d  || 2L
        20L    | 10.0d  || 2L
    }

    def "Get int percent with int percent value"() {
        expect:
        PercentUtil.get(value, percent) == result
        where:
        value | percent || result
        0     | 0        | 0
        0     | 100      | 0
        100   | 100      | 100
        100   | 10       | 10
        100   | 50       | 50
        1     | 50       | 1
    }

    def "Get int value + value percent"() {
        expect:
        PercentUtil.getTotal(value1 as int, value2 as double) == result as int
        where:
        value1 | value2 || result
        0      | 0.0d   || 0
        0      | 1.0d   || 0
        1      | 10.0d  || 1
        4      | 10.0d  || 4
        5      | 10.0d  || 6
        10     | 10.0d  || 11
        14     | 10.0d  || 15
        15     | 10.0d  || 17
        20     | 10.0d  || 22
    }

    def "Get long value + value percent"() {
        expect:
        PercentUtil.getTotal(value1 as long, value2 as double) == result as long
        where:
        value1 | value2 || result
        0L     | 0.0d   || 0L
        0L     | 1.0d   || 0L
        1L     | 10.0d  || 1L
        4L     | 10.0d  || 4L
        5L     | 10.0d  || 6L
        10L    | 10.0d  || 11L
        14L    | 10.0d  || 15L
        15L    | 10.0d  || 17L
        20L    | 10.0d  || 22L
    }

    def "Get double percent from value"() {
        expect:
        PercentUtil.getDecimal(value, percent) == result
        where:
        value | percent || result
        0     | 0        | 0
        0     | 100      | 0
        100   | 100      | 100
        100   | 10       | 10
        100   | 50       | 50
        1     | 50       | 0.5
    }
}
