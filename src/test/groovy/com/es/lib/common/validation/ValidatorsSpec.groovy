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
class ValidatorsSpec extends Specification {

    def "isValidLong"() {
        expect:
        Validators.isValidLong(value) == result
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

    def "isValidDouble"() {
        expect:
        Validators.isValidDouble(value) == result
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

    def "asDouble"() {
        when:
        Validators.asDouble(null)
        then:
        thrown(NullPointerException)
        when:
        Validators.asDouble(value)
        then:
        thrown(NumberFormatException)
        where:
        value << ["", "asd"]
    }

    def "asLong"() {
        when:
        Validators.asLong(null)
        then:
        thrown(NullPointerException)
        when:
        Validators.asLong(value)
        then:
        thrown(NumberFormatException)
        where:
        value << ["", "asd", "11.12", "11,1231"]
    }

    def "asDouble from Object"() {
        expect:
        Validators.asDouble(value) == result
        where:
        value              | result
        "123" as Object    | 123.00d
        123.00d as Object  | 123.00d
        "123.21" as Object | 123.21d
        "123,21" as Object | 123.21d
        123.21d as Object  | 123.21d
    }

    def "asDouble from string"() {
        expect:
        Validators.asDouble(value) == result
        where:
        value    | result
        "123"    | 123.00d
        "123.21" | 123.21d
        "123,21" | 123.21d
    }

    def "asSum"() {
        expect:
        Validators.asSum(value) == result
        where:
        value    | result
        "123"    | 12300
        "123.21" | 12321
        "0,01"   | 1
    }

    def "asLong from Object"() {
        expect:
        Validators.asLong(value) == result
        where:
        value           | result
        "123" as Object | 123
        "0" as Object   | 0
    }

    def "asLong from string"() {
        expect:
        Validators.asLong(value) == result
        where:
        value | result
        "123" | 123
        "0"   | 0
    }

    def "Get int representation throw NPE with null value"() {
        when:
        Validators.asInt(null, 1) == 1
        then:
        thrown(NullPointerException)
    }

    def "Get int representation throw IAE with incorrect index value"() {
        when:
        Validators.asInt("", 1) == 1
        Validators.asInt("", -1) == 1
        then:
        thrown(IllegalArgumentException)
    }

    def "Get int representation of character in string"() {
        expect:
        Validators.asInt(value, index) == result
        where:
        value | index || result
        "1"   | 0     || 1
        "12"  | 0     || 1
        "12"  | 1     || 2
        "99"  | 1     || 9
    }

}
