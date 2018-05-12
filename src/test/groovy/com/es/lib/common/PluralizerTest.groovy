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

package com.es.lib.common

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class PluralizerTest extends Specification {

    def "Evaluate int"() {
        expect:
        Pluralizer.evaluate(value, str1, str2, str3) == result
        where:
        value | str1   | str2  | str3   | result
        0     | 'день' | 'дня' | 'дней' | 'дней'
        1     | 'день' | 'дня' | 'дней' | 'день'
        2     | 'день' | 'дня' | 'дней' | 'дня'
        3     | 'день' | 'дня' | 'дней' | 'дня'
        4     | 'день' | 'дня' | 'дней' | 'дня'
        5     | 'день' | 'дня' | 'дней' | 'дней'
        6     | 'день' | 'дня' | 'дней' | 'дней'
        7     | 'день' | 'дня' | 'дней' | 'дней'
        8     | 'день' | 'дня' | 'дней' | 'дней'
        9     | 'день' | 'дня' | 'дней' | 'дней'
        10    | 'день' | 'дня' | 'дней' | 'дней'
        101   | 'день' | 'дня' | 'дней' | 'день'
        102   | 'день' | 'дня' | 'дней' | 'дня'
    }

    def "Evaluate long"() {
        expect:
        Pluralizer.evaluate(value, str1, str2, str3) == result
        where:
        value | str1   | str2  | str3   | result
        0L    | 'день' | 'дня' | 'дней' | 'дней'
        1L    | 'день' | 'дня' | 'дней' | 'день'
        2L    | 'день' | 'дня' | 'дней' | 'дня'
        3L    | 'день' | 'дня' | 'дней' | 'дня'
        4L    | 'день' | 'дня' | 'дней' | 'дня'
        5L    | 'день' | 'дня' | 'дней' | 'дней'
        6L    | 'день' | 'дня' | 'дней' | 'дней'
        7L    | 'день' | 'дня' | 'дней' | 'дней'
        8L    | 'день' | 'дня' | 'дней' | 'дней'
        9L    | 'день' | 'дня' | 'дней' | 'дней'
        10L   | 'день' | 'дня' | 'дней' | 'дней'
        101L  | 'день' | 'дня' | 'дней' | 'день'
        102L  | 'день' | 'дня' | 'дней' | 'дня'
    }
}
