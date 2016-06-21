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
class SafeNumberUtilSpec extends Specification {

    def "Получение объекта Short"() {
        expect:
        SafeNumberUtil.getShortObject(value1) == result;
        where:
        value1      || result
        null        || null
        10 as short || 10 as short
    }

    def "Получение объекта Integer"() {
        expect:
        SafeNumberUtil.getIntObject(value1) == result;
        where:
        value1 || result
        null   || null
        10     || 10
    }

    def "Получение объекта Long"() {
        expect:
        SafeNumberUtil.getLongObject(value1) == result;
        where:
        value1 || result
        null   || null
        10L    || 10L
    }

    def "Получение объекта Double"() {
        expect:
        SafeNumberUtil.getDoubleObject(value1) == result;
        where:
        value1 || result
        null   || null
        10d    || 10d
    }

    def "Получение double значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getDouble(value1, value2 as double) == result as double;
        where:
        value1 | value2 || result
        null   | 10.0d  || 10.0d
        10.0d  | 11.0d  || 10.0d
    }

    def "Получение double значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getDouble(value1 as Double) == result as double;
        where:
        value1 || result
        null   || 0.0d
        10.0d  || 10.0d
    }

    def "Получение long значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getLong(value1, value2 as long) == result as long;
        where:
        value1 | value2 || result
        null   | 10L    || 10L
        10L    | 11L    || 10L
    }

    def "Получение long значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getLong(value1 as Long) == result as long;
        where:
        value1 || result
        null   || 0L
        10L    || 10L
    }

    def "Получение short значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getShort(value1, value2 as short) == result as short;
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение short значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getShort(value1 as Short) == result as short;
        where:
        value1 || result
        null   || 0
        10     || 10
    }

    def "Получение int значение с указанием числа по умолчанию"() {
        expect:
        SafeNumberUtil.getInt(value1, value2 as int) == result as int;
        where:
        value1 | value2 || result
        null   | 10     || 10
        10     | 11     || 10
    }

    def "Получение int значение без указанием числа по умолчанию (используется 0)"() {
        expect:
        SafeNumberUtil.getInt(value1 as Integer) == result as int;
        where:
        value1 || result
        null   || 0
        10     || 10
    }
}
