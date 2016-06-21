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

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 11.08.15
 */
class StringSplitterSpec extends Specification {

    def "Split"() {
        expect:
        StringSplitter.process(value, splitter) == result
        where:
        value   | splitter                            || result
        ""      | StringSplitter.splitter(";")        || []
        "1;2"   | StringSplitter.splitter(";")        || ["1", "2"]
        "1; 2"  | StringSplitter.splitter(";")        || ["1", "2"]
        "1; 2"  | StringSplitter.splitter(";", true)  || ["1", "2"]
        "1; 2"  | StringSplitter.splitter(";", false) || ["1", " 2"]
        "1;; 2" | StringSplitter.splitter(";", 3)     || ["1", "", "2"]

    }

    def "Split1"() {
        expect:
        StringSplitter.process(value, splitter1, splitter2) == result
        where:
        value        | splitter1                    | splitter2                    || result
        ""           | StringSplitter.splitter(",") | StringSplitter.splitter(":") || []
        "1:2,2:3"    | StringSplitter.splitter(",") | StringSplitter.splitter(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
        "1:2, 2 : 3" | StringSplitter.splitter(",") | StringSplitter.splitter(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
    }
}
