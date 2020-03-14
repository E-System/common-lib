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

package com.es.lib.common.number

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
class NumbersParserSpec extends Specification {

    def "Получение Double значение с указанием числа по умолчанию"() {
        expect:
        Numbers.parser(value1).asDouble(value2 as double) == result as double
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        "asd"  | 10.0d  || 10.0d
        ""     | 10.0d  || 10.0d
        "10.0" | 11.0d  || 10.0d
    }

    def "Получение Double значение без указанием числа по умолчанию (используется null)"() {
        expect:
        Numbers.parser(value1).asDouble() == result as Double
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0d
    }

    def "Получение Float значение с указанием числа по умолчанию"() {
        expect:
        Numbers.parser(value1).asFloat(value2 as float) == result as float
        where:
        value1 | value2 || result
        null   | 10.0f  || 10.0f
        "asd"  | 10.0f  || 10.0f
        ""     | 10.0f  || 10.0f
        "10.0" | 11.0f  || 10.0f
    }

    def "Получение Float значение без указанием числа по умолчанию (используется null)"() {
        expect:
        Numbers.parser(value1).asFloat() == result as Float
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0f
    }

    def "Получение Long значение с указанием числа по умолчанию"() {
        expect:
        Numbers.parser(value1).asLong(value2 as long) == result as long
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        "asd"  | 10L    || 10L
        ""     | 10L    || 10L
        "10"   | 11L    || 10L
    }

    def "Получение Long значение без указанием числа по умолчанию (используется null)"() {
        expect:
        Numbers.parser(value1).asLong() == result as Long
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10L
    }

    def "Получение Short значение с указанием числа по умолчанию"() {
        expect:
        Numbers.parser(value1).asShort(value2 as short) == result as short
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Получение Short значение без указанием числа по умолчанию (используется null)"() {
        expect:
        Numbers.parser(value1).asShort() == result as Short
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }

    def "Получение Integer значение с указанием числа по умолчанию"() {
        expect:
        Numbers.parser(value1).asInt(value2 as int) == result as int
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Получение Integer значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.parser(value1).asInt() == result as Integer
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }
}
