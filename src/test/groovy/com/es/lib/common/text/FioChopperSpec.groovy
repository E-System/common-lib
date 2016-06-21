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

package com.es.lib.common.text

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 06.11.15
 */
class FioChopperSpec extends Specification {

    def "Сокращения с правой стороны"() {
        expect:
        FioChopper.right(value) == result
        where:
        value                          | result
        null                           | null
        ""                             | ""
        "aaa"                          | "aaa"
        "aaa bbb"                      | "aaa b."
        "aaa \t bbb"                   | "aaa b."
        "aaa bbb ccc"                  | "aaa b. c."
        "aaa   bbb   ccc"              | "aaa b. c."
        "aaa  \n bbb   ccc"            | "aaa b. c."
        "aaa   bbb   ccc d."           | "aaa b. c. d."
        "aaa   bbb   ccc d.          " | "aaa b. c. d."
    }

    def "Сокращения с левой стороны"() {
        expect:
        FioChopper.left(value) == result
        where:
        value                          | result
        null                           | null
        ""                             | ""
        "aaa"                          | "aaa"
        "aaa bbb"                      | "b. aaa"
        "aaa \t bbb"                   | "b. aaa"
        "aaa bbb ccc"                  | "b. c. aaa"
        "aaa   bbb   ccc"              | "b. c. aaa"
        "aaa  \n bbb   ccc"            | "b. c. aaa"
        "aaa   bbb   ccc d."           | "b. c. d. aaa"
        "aaa   bbb   ccc d.          " | "b. c. d. aaa"
    }
}
