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
class PhoneUtilSpec extends Specification {

    def "Clean number"() {
        expect:
        PhoneUtil.clean(value) == result
        where:
        value              || result
        null               || null
        ""                 || ""
        "1"                || "1"
        "+7(3852)11-12-12" || "73852111212"
        "+7 905 981 79 16" || "79059817916"
    }

    def "Check on mobile phone"() {
        expect:
        PhoneUtil.isMobile(value) == result
        where:
        value              || result
        null               || false
        ""                 || false
        "1"                || false
        "+7(3852)11-12-12" || false
        "+7 905 981 79 16" || true
        "905 981 79 16"    || true
        "8 905 981 79 16"  || true
        "8-905-981-79-16"  || true
        "111223"           || false
    }

    def "Split input to array of numbers"() {
        expect:
        PhoneUtil.split(value, clean) == result
        where:
        value                                | clean || result
        ""                                   | false || []
        "+7(3852)11-12-12, +7 905 981 79 16" | false || [Pair.of("+7(3852)11-12-12", false), Pair.of("+7 905 981 79 16", true)]
        "+7(3852)11-12-12, +7 905 981 79 16" | true  || [Pair.of("73852111212", false), Pair.of("79059817916", true)]

    }

    def "Join numbers by types"() {
        expect:
        PhoneUtil.joinByType(values, ", ") == result
        where:
        values                                                                                  || result
        []                                                                                      || null
        [Pair.of("11-12-13", false), Pair.of("9059817916", true)]                               || Pair.of("11-12-13", "9059817916")
        [Pair.of("11-12-13", false), Pair.of("9059817916", true), Pair.of("79131111234", true)] || Pair.of("11-12-13", "9059817916, 79131111234")
    }

    def "Split numbers by types"() {
        expect:
        PhoneUtil.groupByType(values, false, ", ") == result
        where:
        values                                            || result
        ""                                                || null
        "+7(3852)11-12-12, +7 905 981 79 16"              || Pair.of("+7(3852)11-12-12", "+7 905 981 79 16")
        "+7(3852)11-12-12"                                || Pair.of("+7(3852)11-12-12", "")
        "+7(3852)11-12-12, +7 905 981 79 16; 79131111234" || Pair.of("+7(3852)11-12-12", "+7 905 981 79 16, 79131111234")
    }

    def "Format number"() {
        expect:
        PhoneUtil.format(value, mask, full) == result
        where:
        value        | mask               | full  || result
        null         | null               | true  || ''
        null         | null               | false || ''
        null         | ""                 | true  || ''
        null         | ""                 | false || ''
        '7913333333' | null               | true  || '7913333333'
        '7913333333' | null               | false || '7913333333'
        '7913333333' | ''                 | true  || '7913333333'
        '7913333333' | ''                 | false || '7913333333'
        '7913333333' | '+7(***) ***-****' | false || '+7(791) 333-3333'
        '79133'      | '+7(***) ***-****' | false || '+7(791) 33'
        '79133'      | '+7(***) ***-****' | true  || '+7(791) 33 -    '
    }
}
