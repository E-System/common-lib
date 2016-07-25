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

package com.es.lib.common.validation

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
class ValidationUtilSpec extends Specification {

    def "Определение корректности целого числа"() {
        expect:
        ValidationUtil.isValidLong(value) == result
        where:
        value   | result
        ""      | false
        "asd"   | false
        "11.22" | false
        "11/22" | false
        "11,22" | false
        "123"   | true
        "222"   | true
    }

    def "Определение корректности дробного числа"() {
        expect:
        ValidationUtil.isValidDouble(value) == result
        where:
        value   | result
        ""      | false
        "asd"   | false
        "11.22" | true
        "11/22" | false
        "11,22" | true
        "123"   | true
        "222"   | true
    }

    def "Исключения парсинга дробного числа"() {
        when:
        ValidationUtil.parseDouble(null)
        then:
        thrown(NullPointerException)
        when:
        ValidationUtil.parseDouble(value)
        then:
        thrown(NumberFormatException)
        where:
        value << ["", "asd"]
    }

    def "Исключения парсинга целого числа"() {
        when:
        ValidationUtil.parseLong(null)
        then:
        thrown(NullPointerException)
        when:
        ValidationUtil.parseLong(value)
        then:
        thrown(NumberFormatException)
        where:
        value << ["", "asd", "11.12", "11,1231"]
    }

    def "Парсинг объекта в дробное число"() {
        expect:
        ValidationUtil.parseDouble(value) == result
        where:
        value              | result
        "123" as Object    | 123.00d
        123.00d as Object  | 123.00d
        "123.21" as Object | 123.21d
        "123,21" as Object | 123.21d
        123.21d as Object  | 123.21d
    }

    def "Парсинг строки в дробное число"() {
        expect:
        ValidationUtil.parseDouble(value) == result
        where:
        value    | result
        "123"    | 123.00d
        "123.21" | 123.21d
        "123,21" | 123.21d
    }

    def "Парсинг суммы в целое число"() {
        expect:
        ValidationUtil.parseSum(value) == result
        where:
        value    | result
        "123"    | 12300
        "123.21" | 12321
        "0,01"   | 1
    }

    def "Парсинг объекта в целое число"() {
        expect:
        ValidationUtil.parseLong(value) == result
        where:
        value           | result
        "123" as Object | 123
        "0" as Object   | 0
    }

    def "Парсинг строки в целое число"() {
        expect:
        ValidationUtil.parseLong(value) == result
        where:
        value | result
        "123" | 123
        "0"   | 0
    }

    def "Get int representation throw NPE with null value"() {
        when:
        ValidationUtil.getInt(null, 1) == 1
        then:
        thrown(NullPointerException)
    }

    def "Get int representation throw IAE with incorrect index value"() {
        when:
        ValidationUtil.getInt("", 1) == 1
        ValidationUtil.getInt("", -1) == 1
        then:
        thrown(IllegalArgumentException)
    }

    def "Get int representation of character in string"() {
        expect:
        ValidationUtil.getInt(value, index) == result
        where:
        value | index || result
        "1"   | 0     || 1
        "12"  | 0     || 1
        "12"  | 1     || 2
        "99"  | 1     || 9
    }

}
