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

    def "Удалить пустые значения"() {
        expect:
        CollectionUtil.removeEmptyValues(map as Map) == result
        where:
        map                                 || result
        null                                || null
        [:]                                 || [:]
        ["k1": null, "k2": "v2"]            || ["k2": "v2"]
        ["k1": "v1", "k2": "v2"]            || ["k1": "v1", "k2": "v2"]
        ["k1": null, "k2": null]            || [:]
        ["k1": "", "k2": "v2"]              || ["k2": "v2"]
        ["k1": "v1", "k2": "v2"]            || ["k1": "v1", "k2": "v2"]
        ["k1": "", "k2": ""]                || [:]
        ["k1": false, "k2": true, "k3": ""] || ["k1": false, "k2": true]
    }

    def "Удалить null значения"() {
        expect:
        CollectionUtil.removeNullValues(map as Map) == result
        where:
        map                                   || result
        null                                  || null
        [:]                                   || [:]
        ["k1": null, "k2": "v2"]              || ["k2": "v2"]
        ["k1": "v1", "k2": "v2"]              || ["k1": "v1", "k2": "v2"]
        ["k1": null, "k2": null]              || [:]
        ["k1": false, "k2": true, "k3": null] || ["k1": false, "k2": true]
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

    def "Fill not available keys with string '1'"() {
        expect:
        CollectionUtil.fillNotAvailableKeys(map, keys, value) == result
        where:
        map        | keys       | value || result
        null       | null       | null  || null
        null       | []         | '1'   || null
        ['A': '1'] | null       | '1'   || ['A': '1']
        ['A': '1'] | ['A']      | '2'   || ['A': '1']
        ['A': '1'] | ['B']      | '2'   || ['A': '1', 'B': '2']
        [:]        | ['A', 'B'] | '1'   || ['A': '1', 'B': '1']
    }

    def "Partition"() {
        expect:
        CollectionUtil.partition(list, count) == result
        where:
        list      | count || result
        []        | 0     || []
        [1]       | 0     || []
        [1]       | 1     || [[1]]
        [1, 2]    | 1     || [[1], [2]]
        [1, 2, 3] | 1     || [[1], [2], [3]]
        [1]       | 2     || [[1]]
        [1, 2]    | 2     || [[1, 2]]
        [1, 2, 3] | 2     || [[1, 2], [3]]
        [1]       | 3     || [[1]]
        [1, 2]    | 3     || [[1, 2]]
        [1, 2, 3] | 3     || [[1, 2, 3]]
        [1, 2, 3] | 4     || [[1, 2, 3]]
    }

    def "PartitionOn shuffle"() {
        expect:
        CollectionUtil.partitionOn(list, count, true) == result
        where:
        list               | count || result
        []                 | 0     || []
        [1]                | 0     || []
        [1]                | 1     || [[1]]
        [1, 2]             | 1     || [[1, 2]]
        [1, 2, 3]          | 1     || [[1, 2, 3]]
        [1]                | 2     || [[1], []]
        [1, 2]             | 2     || [[1], [2]]
        [1, 2, 3]          | 2     || [[1, 3], [2]]
        [1]                | 3     || [[1], [], []]
        [1, 2]             | 3     || [[1], [2], []]
        [1, 2, 3]          | 3     || [[1], [2], [3]]
        [1, 2, 3]          | 4     || [[1], [2], [3], []]
        [1, 2, 3, 4, 5, 6] | 2     || [[1, 3, 5], [2, 4, 6]]
        [1, 2, 3, 4, 5, 6] | 3     || [[1, 4], [2, 5], [3, 6]]
    }

    def "PartitionOn not shuffle"() {
        expect:
        CollectionUtil.partitionOn(list, count, false) == result
        where:
        list               | count || result
        []                 | 0     || []
        [1]                | 0     || []
        [1]                | 1     || [[1]]
        [1, 2]             | 1     || [[1, 2]]
        [1, 2, 3]          | 1     || [[1, 2, 3]]
        [1]                | 2     || [[1], []]
        [1, 2]             | 2     || [[1], [2]]
        [1, 2, 3]          | 2     || [[1, 2], [3]]
        [1]                | 3     || [[1], [], []]
        [1, 2]             | 3     || [[1], [2], []]
        [1, 2, 3]          | 3     || [[1], [2], [3]]
        [1, 2, 3]          | 4     || [[1], [2], [3], []]
        [1, 2, 3, 4, 5, 6] | 2     || [[1, 2, 3], [4, 5, 6]]
        [1, 2, 3, 4, 5, 6] | 3     || [[1, 2], [3, 4], [5, 6]]
        [1, 2, 3, 4, 5]    | 3     || [[1, 2], [3, 4], [5]]
    }

    def "extractByPrefix"() {
        expect:
        CollectionUtil.extractByPrefix(map, prefix, removePrefix) == result
        where:
        map                                                                | prefix     | removePrefix || result
        null                                                               | '123'      | true         || null
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | ''         | true         || [:]
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'KEYWORDS' | true         || ['RU_RU': '1', 'EN_US': '2']
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'OTHER'    | true         || [:]
        ['KEYWORDS_RU_RU': "1", 'KEYWORDS_EN_US': '2', 'OTHER_RU_RU': '3'] | 'KEYWORDS' | true         || ['RU_RU': '1', 'EN_US': '2']
        ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']                           | 'KEYWORDS' | true         || [:]
        null                                                               | '123'      | false        || null
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | ''         | false        || [:]
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'KEYWORDS' | false        || ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'OTHER'    | false        || [:]
        ['KEYWORDS_RU_RU': "1", 'KEYWORDS_EN_US': '2', 'OTHER_RU_RU': '3'] | 'KEYWORDS' | false        || ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']
        ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']                           | 'KEYWORDS' | false        || [:]
    }
}
