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

import java.util.function.Function

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
class PercentsSpec extends Specification {

    def "Get percent value"() {
        expect:
        Percents.value(v1 as int, v2 as int) == result
        where:
        v1  | v2  || result
        100 | 200 || 50.0
        20  | 200 || 10.0
        0   | 100 || 0.0
        100 | 0   || 0.0
        1   | 100 || 1.0
    }

    def "Get percent value (BigDecimal) with null"() {
        expect:
        Percents.value(v1 as BigDecimal, v2 as BigDecimal) == result
        where:
        v1   | v2   || result
        100  | 200  || 50.0
        20   | 200  || 10.0
        0    | 100  || 0.0
        100  | 0    || 0.0
        1    | 100  || 1.0
        null | 100  || 0
        100  | null || 0
        null | null || 0
    }

    def "Get int percent value"() {
        expect:
        Percents.get(value1 as int, value2 as double) == result as int
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
        Percents.get(value1 as long, value2 as double) == result as long
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
        Percents.get(value, percent) == result
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
        Percents.getTotal(value1 as int, value2 as double) == result as int
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
        Percents.getTotal(value1 as long, value2 as double) == result as long
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

    def "Get int value - value percent"() {
        expect:
        Percents.withDiscount(value1 as int, value2 as double) == result as int
        where:
        value1 | value2 || result
        0      | 0.0d   || 0
        0      | 1.0d   || 0
        1      | 10.0d  || 1
        4      | 10.0d  || 4
        5      | 10.0d  || 4
        10     | 10.0d  || 9
        14     | 10.0d  || 13
        15     | 10.0d  || 13
        20     | 10.0d  || 18
    }

    def "Get long value - value percent"() {
        expect:
        Percents.withDiscount(value1 as long, value2 as double) == result as long
        where:
        value1 | value2 || result
        0L     | 0.0d   || 0L
        0L     | 1.0d   || 0L
        1L     | 10.0d  || 1L
        4L     | 10.0d  || 4L
        5L     | 10.0d  || 4L
        10L    | 10.0d  || 9L
        14L    | 10.0d  || 13L
        15L    | 10.0d  || 13L
        20L    | 10.0d  || 18L
    }

    def "Get double percent from value"() {
        expect:
        Percents.getDecimal(value, percent) == result
        where:
        value | percent || result
        0     | 0        | 0
        0     | 100      | 0
        100   | 100      | 100
        100   | 10       | 10
        100   | 50       | 50
        1     | 50       | 0.5
    }

    def "Split with overlap (percent)"() {
        when:
        def res = Percents.split(100, [50d, 50d], true)
        then:
        res.size() == 2
        res[0] == 50
        res[1] == 50

        when:
        res = Percents.split(100, [25d, 25d, 50d], true)
        then:
        res.size() == 3
        res[0] == 25
        res[1] == 25
        res[2] == 50

        when:
        res = Percents.split(131, [25d, 25d, 50d], true)
        then:
        res.size() == 3
        res[0] == 32
        res[1] == 33
        res[2] == 66

        when:
        res = Percents.split(100, [25d, 25d, 55d], true)
        then:
        res.size() == 3
        res[0] == 20
        res[1] == 25
        res[2] == 55
    }

    def "Split sum with invalid values (percents)"() {
        when:
        Percents.split(-1, [], true)
        then:
        thrown(IllegalArgumentException)

        when:
        Percents.split(0, null, true)
        then:
        thrown(IllegalArgumentException)
    }

    def "Split by sum and fullSum"() {
        when:
        def items = [new Item(25000), new Item(25000), new Item(40000), new Item(10000)]
        def res = Percents.split(12300, 100000, items, new Function<Item, Integer>() {

            @Override
            Integer apply(Item t) {
                return t.getSum()
            }
        })
        println res
        then:
        res.size() == 4
        res.sum { it.value } == 12300
    }

    class Item {

        int sum

        Item(int sum) {
            this.sum = sum
        }
    }
}
