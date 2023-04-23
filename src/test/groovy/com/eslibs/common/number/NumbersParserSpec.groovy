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

package com.eslibs.common.number

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
class NumbersParserSpec extends Specification {

    def "Get Double value with default"() {
        expect:
        Numbers.parser(value1).asDouble(value2 as double) == result as double
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        "asd"  | 10.0d  || 10.0d
        ""     | 10.0d  || 10.0d
        "10.0" | 11.0d  || 10.0d
    }

    def "Get Double value without default (use null)"() {
        expect:
        Numbers.parser(value1).asDouble() == result as Double
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0d
    }

    def "Get Float value with default"() {
        expect:
        Numbers.parser(value1).asFloat(value2 as float) == result as float
        where:
        value1 | value2 || result
        null   | 10.0f  || 10.0f
        "asd"  | 10.0f  || 10.0f
        ""     | 10.0f  || 10.0f
        "10.0" | 11.0f  || 10.0f
    }

    def "Get Float value without default (use null)"() {
        expect:
        Numbers.parser(value1).asFloat() == result as Float
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0f
    }

    def "Get Long value with default"() {
        expect:
        Numbers.parser(value1).asLong(value2 as long) == result as long
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        "asd"  | 10L    || 10L
        ""     | 10L    || 10L
        "10"   | 11L    || 10L
    }

    def "Get Long value without default (use null)"() {
        expect:
        Numbers.parser(value1).asLong() == result as Long
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10L
    }

    def "Get Short value with default"() {
        expect:
        Numbers.parser(value1).asShort(value2 as short) == result as short
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Get Short value without default (use null)"() {
        expect:
        Numbers.parser(value1).asShort() == result as Short
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }

    def "Get Integer value with default"() {
        expect:
        Numbers.parser(value1).asInt(value2 as int) == result as int
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Get Integer value without default (use null)"() {
        expect:
        Numbers.parser(value1).asInt() == result as Integer
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }

    def "Get sum value with default"() {
        expect:
        Numbers.parser(value1).asSum(value2 as long) == result as long
        where:
        value1  | value2 || result
        null    | 100    || 100
        "asd"   | 100    || 100
        ""      | 100    || 100
        "10"    | 11     || 1000
        "10.20" | 11     || 1020
        "10,20" | 11     || 1020
    }

    def "Get sum value without default (use null)"() {
        expect:
        Numbers.parser(value1).asSum() == result as Long
        where:
        value1  || result
        null    || null
        "asd"   || null
        ""      || null
        "100"   || 10000
        "10.20" || 1020
        "10,20" || 1020
    }
}
