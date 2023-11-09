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

package com.eslibs.common.number


import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.10.14
 */
class NumbersSpec extends Specification {

    def "Remain with scale"() {
        expect:
        Numbers.remain(a, b, true) == c
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
        Numbers.remain(a, b) == c
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
        Numbers.multiplicity(a, b) == c
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
        Numbers.mult((a * 1000) as long, (b * 1000) as long) == (c * 1000) as long
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
        Numbers.addition((a * 1000) as long, (b * 1000) as long) == (c * 1000) as long
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
        Numbers.additionAndMult(a, b) == c
        where:
        a       | b    || c
        100.0d  | 1.0d || new Numbers.AAM(0.0d, 100.0d)
        0.2d    | 1.0d || new Numbers.AAM(0.8d, 1.0d)
        10.0d   | 3.0d || new Numbers.AAM(2.0d, 12.0d)
        11.0d   | 3.0d || new Numbers.AAM(1.0d, 12.0d)
        11.234d | 3.5d || new Numbers.AAM(2.766d, 14.0d)
        2.12d   | 0.1d || new Numbers.AAM(0.08d, 2.2d)
        10.11d  | 1.0d || new Numbers.AAM(0.89d, 11.0d)
    }

    def "Split sum with invalid values"() {
        when:
        Numbers.splitSum(-1, -1, true)
        then:
        thrown(IllegalArgumentException)

        when:
        Numbers.splitSum(0, -1, true)
        then:
        thrown(IllegalArgumentException)
    }

    def "Split to one element"() {
        when:
        def res = Numbers.splitSum(0, 20, true)
        then:
        res.size() == 1
        res.contains(0)

        when:
        res = Numbers.splitSum(100, 1, true)
        then:
        res.size() == 1
        res.contains(100)
    }

    def "Split with overlap"() {
        when:
        def res = Numbers.splitSum(100, 3, true)
        then:
        res.size() == 3
        res[0] == 34
        res[1] == 33
        res[2] == 33

        when:
        res = Numbers.splitSum(100, 3, false)
        then:
        res.size() == 3
        res[0] == 33
        res[1] == 33
        res[2] == 34

        when:
        res = Numbers.splitSum(133, 3, false)
        then:
        res.size() == 3
        res[0] == 44
        res[1] == 44
        res[2] == 45

        when:
        res = Numbers.splitSum(133, 2, true)
        then:
        res.size() == 2
        res[0] == 67
        res[1] == 66

        when:
        res = Numbers.splitSum(100, 2, true)
        then:
        res.size() == 2
        res[0] == 50
        res[1] == 50
    }

    def "Round"() {
        expect:
        Numbers.Round.MATH.get(10000, 100) == 10000
        Numbers.Round.MATH.get(10001, 100) == 10000
        Numbers.Round.MATH.get(10054, 100) == 10100
        Numbers.Round.MATH.get(10050, 100) == 10100
        Numbers.Round.MATH.get(10049, 100) == 10000
        Numbers.Round.MATH.get(10003, 10) == 10000
        Numbers.Round.MATH.get(10006, 10) == 10010
        Numbers.Round.MATH.get(12654, 100) == 12700
        Numbers.Round.MATH.get(12650, 100) == 12700
        Numbers.Round.MATH.get(12649, 100) == 12600

        Numbers.Round.CEIL.get(10000, 100) == 10000
        Numbers.Round.CEIL.get(10001, 100) == 10100
        Numbers.Round.CEIL.get(10054, 100) == 10100
        Numbers.Round.CEIL.get(10050, 100) == 10100
        Numbers.Round.CEIL.get(10049, 100) == 10100
        Numbers.Round.CEIL.get(10003, 10) == 10010
        Numbers.Round.CEIL.get(10006, 10) == 10010
        Numbers.Round.CEIL.get(12654, 100) == 12700
        Numbers.Round.CEIL.get(12650, 100) == 12700
        Numbers.Round.CEIL.get(12649, 100) == 12700

        Numbers.Round.FLOOR.get(10000, 100) == 10000
        Numbers.Round.FLOOR.get(10001, 100) == 10000
        Numbers.Round.FLOOR.get(10054, 100) == 10000
        Numbers.Round.FLOOR.get(10050, 100) == 10000
        Numbers.Round.FLOOR.get(10049, 100) == 10000
        Numbers.Round.FLOOR.get(10003, 10) == 10000
        Numbers.Round.FLOOR.get(10006, 10) == 10000
        Numbers.Round.FLOOR.get(12654, 100) == 12600
        Numbers.Round.FLOOR.get(12650, 100) == 12600
        Numbers.Round.FLOOR.get(12649, 100) == 12600
    }
}
