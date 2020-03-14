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
class NumbersConverterSpec extends Specification {

    def "Получение объекта Short"() {
        expect:
        Numbers.converter(value1).asShortObject() == result
        where:
        value1      || result
        null        || null
        10 as short || 10 as short
    }

    def "Получение объекта Integer"() {
        expect:
        Numbers.converter(value1).asIntObject() == result
        where:
        value1 || result
        null   || null
        10     || 10
    }

    def "Получение объекта Long"() {
        expect:
        Numbers.converter(value1).asLongObject() == result
        where:
        value1 || result
        null   || null
        10L    || 10L
    }

    def "Получение объекта Float"() {
        expect:
        Numbers.converter(value1).asFloatObject() == result
        where:
        value1 || result
        null   || null
        10f    || 10f
    }

    def "Получение объекта Double"() {
        expect:
        Numbers.converter(value1).asDoubleObject() == result
        where:
        value1 || result
        null   || null
        10d    || 10d
    }

    def "Получение double значение с указанием числа по умолчанию"() {
        expect:
        Numbers.converter(value1).asDouble(value2 as double) == result as double
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        10.0d  | 11.0d  || 10.0d
    }

    def "Получение double значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.converter(value1 as Double).asDouble() == result as double
        where:
        value1 || result
        null   || 0.0d
        10.0d  || 10.0d
    }

    def "Получение float значение с указанием числа по умолчанию"() {
        expect:
        Numbers.converter(value1).asFloat(value2 as float) == result as float
        where:
        value1 | value2 || result
        null   | 10.0f  || 10.0f
        10.0f  | 11.0f  || 10.0f
    }

    def "Получение float значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.converter(value1 as Float).asFloat() == result as float
        where:
        value1 || result
        null   || 0.0f
        10.0f  || 10.0f
    }

    def "Получение long значение с указанием числа по умолчанию"() {
        expect:
        Numbers.converter(value1).asLong(value2 as long) == result as long
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        10L    | 11L    || 10L
    }

    def "Получение long значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.converter(value1 as Long).asLong() == result as long
        where:
        value1 || result
        null   || 0L
        10L    || 10L
    }


    def "Получение short значение с указанием числа по умолчанию"() {
        expect:
        Numbers.converter(value1).asShort(value2 as short) == result as short
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение short значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.converter(value1 as Short).asShort() == result as short
        where:
        value1 || result
        null   || 0
        10     || 10
    }

    def "Получение int значение с указанием числа по умолчанию"() {
        expect:
        Numbers.converter(value1).asInt(value2 as int) == result as int
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение int значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        Numbers.converter(value1 as Integer).asInt() == result as int
        where:
        value1 || result
        null   || 0
        10     || 10
    }
}
