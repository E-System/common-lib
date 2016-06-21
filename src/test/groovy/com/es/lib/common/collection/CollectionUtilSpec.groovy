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

package com.es.lib.common.collection

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class CollectionUtilSpec extends Specification {

    def "Удалить null значения"() {
        expect:
        CollectionUtil.removeNullValues(map as Map) == result
        where:
        map                      | result
        null                     | null
        [:]                      | [:]
        ["k1": null, "k2": "v2"] | ["k2": "v2"]
        ["k1": "v1", "k2": "v2"] | ["k1": "v1", "k2": "v2"]
        ["k1": null, "k2": null] | [:]
    }

    def "Получение первого элемента или null"() {
        expect:
        CollectionUtil.getFirstOrNull(list) == result
        where:
        list   | result
        null   | null
        []     | null
        [1]    | 1
        [1, 2] | 1
    }

    def "Заджоинить не пустые элементы"() {
        expect:
        CollectionUtil.joinNotBlank(values, delimiter) == result
        where:
        values                               | delimiter || result
        ["Hello", "", null]                  | ", "      || "Hello"
        [null, "Hello", "", null]            | ", "      || "Hello"
        ["Hello", "123", null]               | ", "      || "Hello, 123"
        ["44-15-12", "+79059817916", null]   | ", "      || "44-15-12, +79059817916"
        ["44-15-12", " ", " "]               | ", "      || "44-15-12"
        ["44-15-12", "+79059817916", "    "] | ", "      || "44-15-12, +79059817916"
    }

    def "Проверка признака равенства null значения пары"() {
        expect:
        CollectionUtil.isValueNull(entry) == result
        where:
        entry                                     || result
        null                                      || true
        new AbstractMap.SimpleEntry("abc", null)  || true
        new AbstractMap.SimpleEntry("abc", "")    || false
        new AbstractMap.SimpleEntry("abc", "asd") || false
    }

    def "Проверка признака неравенства null значения пары"() {
        expect:
        CollectionUtil.isValueNonNull(entry) == result
        where:
        entry                                     || result
        null                                      || false
        new AbstractMap.SimpleEntry("abc", null)  || false
        new AbstractMap.SimpleEntry("abc", "")    || true
        new AbstractMap.SimpleEntry("abc", "asd") || true
    }

    def "Проверка признака равенства null ключа пары"() {
        expect:
        CollectionUtil.isKeyNull(entry) == result
        where:
        entry                                     || result
        null                                      || true
        new AbstractMap.SimpleEntry(null, "abc")  || true
        new AbstractMap.SimpleEntry("", "abc")    || false
        new AbstractMap.SimpleEntry("abc", "asd") || false
    }

    def "Проверка признака неравенства null ключа пары"() {
        expect:
        CollectionUtil.isKeyNonNull(entry) == result
        where:
        entry                                     || result
        null                                      || false
        new AbstractMap.SimpleEntry(null, "abc")  || false
        new AbstractMap.SimpleEntry("", "abc")    || true
        new AbstractMap.SimpleEntry("abc", "asd") || true
    }
}
