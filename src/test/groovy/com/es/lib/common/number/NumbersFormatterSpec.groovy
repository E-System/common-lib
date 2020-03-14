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

import java.text.DecimalFormatSymbols

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.10.14
 */
class NumbersFormatterSpec extends Specification {

    static dfs = new DecimalFormatSymbols().groupingSeparator

    def "Format"() {
        expect:
        Numbers.formatter().format(a) == b
        where:
        a              || b
        0.001d         || "0,00"
        0.01d          || "0,01"
        1.0d           || "1,00"
        2.2d           || "2,20"
        3.333d         || "3,33"
        1000000.00001f || "1000000,00"
    }

    def "Format money"() {
        expect:
        Numbers.formatter().money(a) == b
        where:
        a   || b
        1   || "0,01"
        10  || "0,10"
        100 || "1,00"
    }

    def "Format with char point delimiter"() {
        expect:
        Numbers.formatter(".").format(a) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1000000.00"
    }

    def "Format with char point delimiter money"() {
        expect:
        Numbers.formatter(".").money(a) == b
        where:
        a   || b
        1   || "0.01"
        10  || "0.10"
        100 || "1.00"
    }

    def "Format with char point delimiter with grouping size 2"() {
        expect:
        Numbers.formatter(2, false, ".", 2).format(a,) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "00" + dfs + "00" + dfs + "00.00"
    }

    def "Format with char point delimiter with grouping size 2 money"() {
        expect:
        Numbers.formatter(2, false, ".", 2).money(a,) == b
        where:
        a   || b
        1   || "0.01"
        10  || "0.10"
        100 || "1.00"
    }

    def "Format with char point delimiter with grouping size 3"() {
        expect:
        Numbers.formatter(2, false, ".", 3).format(a) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "000" + dfs + "000.00"
    }

    def "Format with char point delimiter with grouping size 3 money"() {
        expect:
        Numbers.formatter(2, false, ".", 3).money(a) == b
        where:
        a   || b
        1   || "0.01"
        10  || "0.10"
        100 || "1.00"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes"() {
        expect:
        Numbers.formatter(2, true, ".", 3).format(a) == b
        where:
        a              || b
        0.001d         || "0"
        0.01d          || "0.01"
        1.0d           || "1"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "000" + dfs + "000"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes money"() {
        expect:
        Numbers.formatter(2, true, ".", 3).money(a) == b
        where:
        a   || b
        1   || "0.01"
        10  || "0.10"
        100 || "1"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes and 3 decimals"() {
        expect:
        Numbers.formatter(3, true, ".", 3).format(a) == b
        where:
        a              || b
        0.001d         || "0.001"
        0.01d          || "0.010"
        1.0d           || "1"
        2.2d           || "2.200"
        3.333d         || "3.333"
        1000000.00001f || "1" + dfs + "000" + dfs + "000"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes and 3 decimals money"() {
        expect:
        Numbers.formatter(3, true, ".", 3).money(a) == b
        where:
        a              || b
        1              || "0.010"
        10             || "0.100"
        100            || "1"
        1000000.00001f || "10" + dfs + "000"
    }
}
