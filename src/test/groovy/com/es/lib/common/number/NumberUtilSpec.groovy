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

import com.es.lib.common.number.NumberFormatUtil
import com.es.lib.common.number.NumberUtil
import spock.lang.Specification

import java.text.DecimalFormatSymbols

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.10.14
 */
class NumberUtilSpec extends Specification {

    static dfs = new DecimalFormatSymbols().groupingSeparator

    def "Format"() {
        expect:
        NumberFormatUtil.f22(a) == b
        where:
        a              || b
        0.001d         || "0,00"
        0.01d          || "0,01"
        1.0d           || "1,00"
        2.2d           || "2,20"
        3.333d         || "3,33"
        1000000.00001f || "1000000,00"
        1              || "0,01"
        10             || "0,10"
        100            || "1,00"
    }

    def "Format with char point delimiter"() {
        expect:
        NumberFormatUtil.f22(a, '.') == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1000000.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with string point delimiter"() {
        expect:
        NumberFormatUtil.f22(a, ".") == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1000000.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with char point delimiter with grouping size 2"() {
        expect:
        NumberFormatUtil.f22(a, '.', 2) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "00" + dfs + "00" + dfs + "00.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with string point delimiter with grouping size 2"() {
        expect:
        NumberFormatUtil.f22(a, ".", 2) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "00" + dfs + "00" + dfs + "00.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with char point delimiter with grouping size 3"() {
        expect:
        NumberFormatUtil.f22(a, '.', 3) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "000" + dfs + "000.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with string point delimiter with grouping size 3"() {
        expect:
        NumberFormatUtil.f22(a, ".", 3) == b
        where:
        a              || b
        0.001d         || "0.00"
        0.01d          || "0.01"
        1.0d           || "1.00"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "000" + dfs + "000.00"
        1              || "0.01"
        10             || "0.10"
        100            || "1.00"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes"() {
        expect:
        NumberFormatUtil.f22(a, ".", 3, true) == b
        where:
        a              || b
        0.001d         || "0"
        0.01d          || "0.01"
        1.0d           || "1"
        2.2d           || "2.20"
        3.333d         || "3.33"
        1000000.00001f || "1" + dfs + "000" + dfs + "000"
        1              || "0.01"
        10             || "0.10"
        100            || "1"
    }

    def "Format with string point delimiter with grouping size 3 and chop zeroes and 3 decimals"() {
        expect:
        NumberFormatUtil.format(a, ".", 3, 3, true) == b
        where:
        a              || b
        0.001d         || "0.001"
        0.01d          || "0.010"
        1.0d           || "1"
        2.2d           || "2.200"
        3.333d         || "3.333"
        1000000.00001f || "1" + dfs + "000" + dfs + "000"
        1              || "0.010"
        10             || "0.100"
        100            || "1"
    }


    def "Format with null or empty string point delimiter throw exception"() {
        when:
        NumberFormatUtil.f22(a, b)
        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Need define at least one symbol in string"
        where:
        a      || b
        0.001d || ""
        0.01d  || null
    }


    def "Remain with scale"() {
        expect:
        NumberUtil.remain(a, b, true) == c
        where:
        a       | b    || c
        0.2d    | 1.0d || 0.8d
        10.0d   | 3.0d || 1.0d
        11.0d   | 3.0d || 2.0d
        11.234d | 3.5d || 0.734d
        2.12d   | 0.1d || 0.02d
        10.11d  | 1.0d || 0.1099999d
    }

    def "Remain without scale(BG)"() {
        expect:
        NumberUtil.remain(a, b) == c
        where:
        a       | b    || c
        0.2d    | 1.0d || 0.2d
        10.0d   | 3.0d || 1.0d
        11.0d   | 3.0d || 2.0d
        11.234d | 3.5d || 0.734d
        2.12d   | 0.1d || 0.02d
        10.11d  | 1.0d || 0.11d
    }

    def "Multiplicity"() {
        expect:
        NumberUtil.multiplicity(a, b) == c
        where:
        a       | b    || c
        100.0d  | 1.0d || 100.0d
        0.2d    | 1.0d || 1.0d
        10.0d   | 3.0d || 12.0d
        11.0d   | 3.0d || 12.0d
        11.234d | 3.5d || 14.0d
        2.12d   | 0.1d || 2.2d
        10.11d  | 1.0d || 11.0d
    }

    def "Mult"() {
        expect:
        NumberUtil.mult((a * 1000) as long, (b * 1000) as long) == (c * 1000) as long
        where:
        a       | b    || c
        100.0d  | 1.0d || 100.0d
        0.2d    | 1.0d || 1.0d
        10.0d   | 3.0d || 12.0d
        11.0d   | 3.0d || 12.0d
        11.234d | 3.5d || 14.0d
        2.12d   | 0.1d || 2.2d
        10.11d  | 1.0d || 11.0d
    }

    def "Addition"() {
        expect:
        NumberUtil.addition((a * 1000) as long, (b * 1000) as long) == (c * 1000) as long
        where:
        a       | b    || c
        100.0d  | 1.0d || 0.0d
        0.2d    | 1.0d || 0.8d
        10.0d   | 3.0d || 2.0d
        11.0d   | 3.0d || 1.0d
        11.234d | 3.5d || 2.766d
        2.12d   | 0.1d || 0.08d
        10.11d  | 1.0d || 0.89d
    }

    def "Addition and mult"() {
        expect:
        NumberUtil.additionAndMult(a, b) == c
        where:
        a       | b    || c
        100.0d  | 1.0d || new NumberUtil.AAM(0.0d, 100.0d)
        0.2d    | 1.0d || new NumberUtil.AAM(0.8d, 1.0d)
        10.0d   | 3.0d || new NumberUtil.AAM(2.0d, 12.0d)
        11.0d   | 3.0d || new NumberUtil.AAM(1.0d, 12.0d)
        11.234d | 3.5d || new NumberUtil.AAM(2.766d, 14.0d)
        2.12d   | 0.1d || new NumberUtil.AAM(0.08d, 2.2d)
        10.11d  | 1.0d || new NumberUtil.AAM(0.89d, 11.0d)
    }
}