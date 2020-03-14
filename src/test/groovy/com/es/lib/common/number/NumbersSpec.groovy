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
}
