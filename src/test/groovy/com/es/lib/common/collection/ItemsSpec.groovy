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

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class ItemsSpec extends Specification {

    def "not null map mutable"() {
        when:
        def map = [key: 'value']
        def map2 = Items.map(map)
        map['key2'] = 'value2'
        map2['key3'] = 'value3'
        then:
        Items.map(null) != null
        Items.map([:]) != null
        !Items.map([key: 'value']).isEmpty()
        map2.containsKey('key2')
        map2.containsKey('key3')
        map.containsKey('key2')
        map.containsKey('key3')
    }

    def "not null map immutable param"() {
        when:
        def map = [key: 'value']
        def map2 = Items.map(map, true)
        map['key2'] = 'value2'
        map2['key3'] = 'value3'
        then:
        Items.map(null, true) != null
        Items.map([:], true) != null
        !Items.map([key: 'value'], true).isEmpty()
        !map2.containsKey('key2')
        map2.containsKey('key3')
        map.containsKey('key2')
        !map.containsKey('key3')
    }

    def "immutableMap"() {
        when:
        def map = [key: 'value']
        def map2 = Items.immutableMap(map)
        map['key2'] = 'value2'
        map2['key3'] = 'value3'
        then:
        Items.immutableMap(null) == null
        Items.immutableMap([:]) != null
        !Items.immutableMap([key: 'value']).isEmpty()
        !map2.containsKey('key2')
        map2.containsKey('key3')
        map.containsKey('key2')
        !map.containsKey('key3')
    }

    def "immutableSet"() {
        when:
        def set = new HashSet<String>(Arrays.asList('value'))
        def set2 = Items.immutableSet(set)
        set.add('value2')
        set2.add('value3')
        then:
        Items.immutableSet(null) == null
        Items.immutableSet(new HashSet()) != null
        !Items.immutableSet(new HashSet(Arrays.asList('value'))).isEmpty()
        !set2.contains('value2')
        set2.contains('value3')
        set.contains('value2')
        !set.contains('value3')
    }

    def "Remove empty values"() {
        expect:
        Items.removeEmptyValues(map as Map) == result
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

    def "Remove null values"() {
        expect:
        Items.removeNullValues(map as Map) == result
        where:
        map                                   || result
        null                                  || null
        [:]                                   || [:]
        ["k1": null, "k2": "v2"]              || ["k2": "v2"]
        ["k1": "v1", "k2": "v2"]              || ["k1": "v1", "k2": "v2"]
        ["k1": null, "k2": null]              || [:]
        ["k1": false, "k2": true, "k3": null] || ["k1": false, "k2": true]
    }

    def "Get first or null"() {
        expect:
        Items.getFirst(list) == result
        where:
        list   | result
        null   | null
        []     | null
        [1]    | 1
        [1, 2] | 1
    }

    def "Join not empty values"() {
        expect:
        Items.joinNotBlank(values, delimiter) == result
        where:
        values                               | delimiter || result
        ["Hello", "", null]                  | ", "      || "Hello"
        [null, "Hello", "", null]            | ", "      || "Hello"
        ["Hello", "123", null]               | ", "      || "Hello, 123"
        ["44-15-12", "+79059817916", null]   | ", "      || "44-15-12, +79059817916"
        ["44-15-12", " ", " "]               | ", "      || "44-15-12"
        ["44-15-12", "+79059817916", "    "] | ", "      || "44-15-12, +79059817916"
    }

    def "Check pair value is null"() {
        expect:
        Items.isValueNull(entry) == result
        where:
        entry                                     || result
        null                                      || true
        new AbstractMap.SimpleEntry("abc", null)  || true
        new AbstractMap.SimpleEntry("abc", "")    || false
        new AbstractMap.SimpleEntry("abc", "asd") || false
    }

    def "Check pair value is not null"() {
        expect:
        Items.isValueNonNull(entry) == result
        where:
        entry                                     || result
        null                                      || false
        new AbstractMap.SimpleEntry("abc", null)  || false
        new AbstractMap.SimpleEntry("abc", "")    || true
        new AbstractMap.SimpleEntry("abc", "asd") || true
    }

    def "Check pair key is null"() {
        expect:
        Items.isKeyNull(entry) == result
        where:
        entry                                     || result
        null                                      || true
        new AbstractMap.SimpleEntry(null, "abc")  || true
        new AbstractMap.SimpleEntry("", "abc")    || false
        new AbstractMap.SimpleEntry("abc", "asd") || false
    }

    def "Check pair key is not null"() {
        expect:
        Items.isKeyNonNull(entry) == result
        where:
        entry                                     || result
        null                                      || false
        new AbstractMap.SimpleEntry(null, "abc")  || false
        new AbstractMap.SimpleEntry("", "abc")    || true
        new AbstractMap.SimpleEntry("abc", "asd") || true
    }

    def "Fill not available keys with string '1'"() {
        expect:
        Items.fillNotAvailableKeys(map, keys, value) == result
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
        Items.partition(list, count) == result
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
        Items.partitionOn(list, count, true) == result
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
        Items.partitionOn(list, count, false) == result
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
        Items.extractByPrefix(map, prefix, removePrefix) == result
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

    def "removeByPrefix"() {
        expect:
        Items.removeByPrefix(map, prefix) == result
        where:
        map                                                                | prefix      || result
        null                                                               | '123'       || null
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | ''          || ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'KEYWORDS'  || [:]
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'OTHER'     || ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']
        ['KEYWORDS_RU_RU': "1", 'KEYWORDS_EN_US': '2', 'OTHER_RU_RU': '3'] | 'KEYWORDS'  || ['OTHER_RU_RU': '3']
        ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']                           | 'KEYWORDS'  || ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'KEYWORDS_' || [:]
        ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']                     | 'OTHER_'    || ['KEYWORDS_RU_RU': '1', 'KEYWORDS_EN_US': '2']
        ['KEYWORDS_RU_RU': "1", 'KEYWORDS_EN_US': '2', 'OTHER_RU_RU': '3'] | 'KEYWORDS_' || ['OTHER_RU_RU': '3']
        ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']                           | 'KEYWORDS_' || ['OTHER_RU_RU': '1', 'OTHER_EN_US': '2']
    }

    def "FindWithIndex"() {
        expect:
        Items.findWithIndex(input, new Predicate<Object>() {
            @Override
            boolean test(Object o) {
                return o == 1
            }
        }) == result
        where:
        input     || result
        null      || null
        []        || null
        [1, 2, 3] || Pair.of(1, 0)
        [2, 1, 3] || Pair.of(1, 1)
        [3, 2, 1] || Pair.of(1, 2)
        [3, 2, 4] || null
    }

    def "Update values with null"() {
        when:
        def attributes = null
        def supplier = new Supplier<Map<String, String>>() {
            @Override
            Map<String, String> get() {
                return attributes
            }
        }
        def consumer = new Consumer<Map<String, String>>() {
            @Override
            void accept(Map<String, String> o) {
                attributes = o
            }
        }
        def newAttributes = Items.updateValues(supplier, consumer, [Pair.of('K1', 'V1'), Pair.of('K2', 'V2'), Pair.of('K3', null), Pair.of('K4', '')])
        then:
        attributes == newAttributes
        newAttributes.containsKey('K1')
        newAttributes.containsKey('K2')
        !newAttributes.containsKey('K3')
        !newAttributes.containsKey('K4')
        newAttributes['K1'] == 'V1'
        newAttributes['K2'] == 'V2'
    }

    def "Update values from sources with null"() {
        when:
        def attributes = null
        def supplier = new Supplier<Map<String, String>>() {
            @Override
            Map<String, String> get() {
                return attributes
            }
        }
        def consumer = new Consumer<Map<String, String>>() {
            @Override
            void accept(Map<String, String> o) {
                attributes = o
            }
        }
        def newAttributes = Items.updateValues(supplier, consumer, ['K1': 'V1', 'K2': 'V2', 'K3': null, 'K4': ''], ['K1', 'K2', 'K3', 'K4'])
        then:
        attributes == newAttributes
        newAttributes.containsKey('K1')
        newAttributes.containsKey('K2')
        !newAttributes.containsKey('K3')
        !newAttributes.containsKey('K4')
        newAttributes['K1'] == 'V1'
        newAttributes['K2'] == 'V2'
    }

    def "Update values from sources with null with not all keys"() {
        when:
        def attributes = null
        def supplier = new Supplier<Map<String, String>>() {
            @Override
            Map<String, String> get() {
                return attributes
            }
        }
        def consumer = new Consumer<Map<String, String>>() {
            @Override
            void accept(Map<String, String> o) {
                attributes = o
            }
        }
        def newAttributes = Items.updateValues(supplier, consumer, ['K1': 'V1', 'K2': 'V2', 'K3': null, 'K4': ''], ['K1', 'K3', 'K4'])
        then:
        attributes == newAttributes
        newAttributes.containsKey('K1')
        !newAttributes.containsKey('K2')
        !newAttributes.containsKey('K3')
        !newAttributes.containsKey('K4')
        newAttributes['K1'] == 'V1'
    }

    def "coalesce not null"() {
        expect:
        Items.coalesce(null, null, 'Hello') == 'Hello'
        Items.coalesce(null, null, 'Hello', null, null) == 'Hello'
        Items.coalesce('Hello', null, null) == 'Hello'
        Items.coalesce('', null, null) == ''
    }

    def "first not empty"() {
        setup:
        def predicate = new Predicate<String>() {
            @Override
            boolean test(String t) {
                return StringUtils.isNotBlank(t)
            }
        }
        expect:
        Items.firstBySelector(predicate, null, null, 'Hello') == 'Hello'
        Items.firstBySelector(predicate, null, null, 'Hello', null, null) == 'Hello'
        Items.firstBySelector(predicate, 'Hello', null, null) == 'Hello'
        Items.firstBySelector(predicate, '', 'Hello', null) == 'Hello'
        Items.firstBySelector(predicate, '', null, 'Hello', null) == 'Hello'
    }

    def "groupBy"() {
        when:
        def items = [new GroupClass("1", "1"), new GroupClass("1", "2"), new GroupClass("2", "1")]
        then:
        def result = Items.groupBy(items, new Function<GroupClass, String>() {
            @Override
            String apply(GroupClass t) {
                return t.v1
            }
        })
        expect:
        result["1"].size() == 2
        result["1"][0].v2 == "1"
        result["1"][1].v2 == "2"
        result["2"].size() == 1
        result["2"][0].v2 == "1"
    }

    def "groupBy with null list"() {
        expect:
        Items.groupBy(null, null) == [:]
    }

    def "toMap with entry"() {
        when:
        def res = Items.toMap([Pair.of("1", "2"), Pair.of("2", "3")])
        then:
        res.size() == 2
        res["1"] == "2"
        res["2"] == "3"
    }

    def "toMap with value mapper"() {
        when:
        def res = Items.toMap([new GroupClass("1", "2"), new GroupClass("2", "3")], { return it.v1 }, { return it })
        then:
        res.size() == 2
        res["1"].v2 == "2"
        res["2"].v2 == "3"
    }

    def "toMap with value mapper from stream"() {
        when:
        def res = Items.toMap([new GroupClass("1", "2"), new GroupClass("2", "3")].stream(), { return it.v1 }, { return it })
        then:
        res.size() == 2
        res["1"].v2 == "2"
        res["2"].v2 == "3"
    }

    def "toMap without value mapper"() {
        when:
        def res = Items.toMap([new GroupClass("1", "2"), new GroupClass("2", "3")], { return it.v1 })
        then:
        res.size() == 2
        res["1"].v2 == "2"
        res["2"].v2 == "3"
    }

    def "toMap without value mapper from stream"() {
        when:
        def res = Items.toMap([new GroupClass("1", "2"), new GroupClass("2", "3")].stream(), { return it.v1 })
        then:
        res.size() == 2
        res["1"].v2 == "2"
        res["2"].v2 == "3"
    }

    def "toMap with empty and null"() {
        expect:
        Items.toMap([]) == [:]
        Items.toMap(null) == [:]
    }

    class GroupClass {
        String v1
        String v2

        GroupClass(String v1, String v2) {
            this.v1 = v1
            this.v2 = v2
        }
    }

    def "Cycle with empty items return null"() {
        expect:
        Items.cycle({ return null }, { return 0 }, {}) == null
        Items.cycle({ return [] }, { return 0 }, {}) == null
    }

    def "Cycle with one element return same multiple times"() {
        when:
        def index = 0
        def link1 = "link1"
        def first = Items.cycle({ return [link1] }, { return index }, { index = it })
        def second = Items.cycle({ return [link1] }, { return index }, { index = it })
        def third = Items.cycle({ return [link1] }, { return index }, { index = it })
        then:
        first == link1
        first == second
        second == third
    }

    def "Cycle with two element"() {
        when:
        def index = 0
        def link1 = "link1"
        def link2 = "link2"
        def first = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        def second = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        def third = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        then:
        first == link1
        second == link2
        third == link1
    }

    def "Cycle with two element and default overflow index set index to 0"() {
        when:
        def index = 2
        def link1 = "link1"
        def link2 = "link2"
        def first = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        def second = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        def third = Items.cycle({ return [link1, link2] }, { return index }, { index = it })
        then:
        first == link1
        second == link2
        third == link1
    }

    def "Cycle with three element"() {
        when:
        def index = 0
        def link1 = "link1"
        def link2 = "link2"
        def link3 = "link3"
        def first = Items.cycle({ return [link1, link2, link3] }, { return index }, { index = it })
        def second = Items.cycle({ return [link1, link2, link3] }, { return index }, { index = it })
        def third = Items.cycle({ return [link1, link2, link3] }, { return index }, { index = it })
        def fourth = Items.cycle({ return [link1, link2, link3] }, { return index }, { index = it })
        then:
        first == link1
        second == link2
        third == link3
        fourth == link1
    }

    def "Remove"() {
        expect:
        Items.remove(null, null) == null
        Items.remove([:], null) == [:]
        Items.remove([:], ['a']) == [:]
        Items.remove(['b': 'b'], ['a']) == ['b': 'b']
        Items.remove(['b': 'b'], ['b']) == [:]
        Items.remove(['a': 'a', 'b': 'b'], ['a']) == ['b': 'b']
    }

    def "Convert"() {
        expect:
        Items.convert(null, { String.valueOf(it) }) == null
        Items.convert([], { String.valueOf(it) }) == null
        Items.convert(null, false, { String.valueOf(it) }) == []
        Items.convert([], false, { String.valueOf(it) }) == []
        Items.convert([1, 2], { String.valueOf(it) }) == ["1", "2"]
    }
}
