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

import com.es.lib.common.number.SafeNumberUtil
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
class SafeNumberUtilSpec extends Specification {

    def "Получение объекта Short"() {
        expect:
        SafeNumberUtil.getShortObject(value1) == result
        where:
        value1      || result
        null        || null
        10 as short || 10 as short
    }

    def "Получение объекта Integer"() {
        expect:
        SafeNumberUtil.getIntObject(value1) == result
        where:
        value1 || result
        null   || null
        10     || 10
    }

    def "Получение объекта Long"() {
        expect:
        SafeNumberUtil.getLongObject(value1) == result
        where:
        value1 || result
        null   || null
        10L    || 10L
    }

    def "Получение объекта Double"() {
        expect:
        SafeNumberUtil.getDoubleObject(value1) == result
        where:
        value1 || result
        null   || null
        10d    || 10d
    }

    def "Получение double значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getDouble(value1, value2 as double) == result as double
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        10.0d  | 11.0d  || 10.0d
    }

    def "Получение Double значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.parseDouble(value1, value2 as double) == result as double
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        "asd"  | 10.0d  || 10.0d
        ""     | 10.0d  || 10.0d
        "10.0" | 11.0d  || 10.0d
    }

    def "Получение double значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getDouble(value1 as Double) == result as double
        where:
        value1 || result
        null   || 0.0d
        10.0d  || 10.0d
    }

    def "Получение Double значение без указанием числа по умолчанию (используется null)"() {
        expect:
        SafeNumberUtil.parseDouble(value1) == result as Double
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0d
    }

    def "Получение float значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getFloat(value1, value2 as float) == result as float
        where:
        value1 | value2 || result
        null   | 10.0f  || 10.0f
        10.0f  | 11.0f  || 10.0f
    }

    def "Получение Float значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.parseFloat(value1, value2 as float) == result as float
        where:
        value1 | value2 || result
        null   | 10.0f  || 10.0f
        "asd"  | 10.0f  || 10.0f
        ""     | 10.0f  || 10.0f
        "10.0" | 11.0f  || 10.0f
    }

    def "Получение float значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getFloat(value1 as Float) == result as float
        where:
        value1 || result
        null   || 0.0f
        10.0f  || 10.0f
    }

    def "Получение Float значение без указанием числа по умолчанию (используется null)"() {
        expect:
        SafeNumberUtil.parseFloat(value1) == result as Float
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10.0" || 10.0f
    }

    def "Получение long значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getLong(value1, value2 as long) == result as long
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        10L    | 11L    || 10L
    }

    def "Получение Long значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.parseLong(value1, value2 as long) == result as long
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        "asd"  | 10L    || 10L
        ""     | 10L    || 10L
        "10"   | 11L    || 10L
    }

    def "Получение long значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getLong(value1 as Long) == result as long
        where:
        value1 || result
        null   || 0L
        10L    || 10L
    }

    def "Получение Long значение без указанием числа по умолчанию (используется null)"() {
        expect:
        SafeNumberUtil.parseLong(value1) == result as Long
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10L
    }

    def "Получение short значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getShort(value1, value2 as short) == result as short
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение Short значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.parseShort(value1, value2 as short) == result as short
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Получение short значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getShort(value1 as Short) == result as short
        where:
        value1 || result
        null   || 0
        10     || 10
    }

    def "Получение Short значение без указанием числа по умолчанию (используется null)"() {
        expect:
        SafeNumberUtil.parseShort(value1) == result as Short
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }

    def "Получение int значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getInt(value1, value2 as int) == result as int
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение Integer значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.parseInt(value1, value2 as int) == result as int
        where:
        value1 | value2 || result
        null   | 10     || 10
        "asd"  | 10     || 10
        ""     | 10     || 10
        "10"   | 11     || 10
    }

    def "Получение int значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getInt(value1 as Integer) == result as int
        where:
        value1 || result
        null   || 0
        10     || 10
    }

    def "Получение Integer значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.parseInt(value1) == result as Integer
        where:
        value1 || result
        null   || null
        "asd"  || null
        ""     || null
        "10"   || 10
    }
}
