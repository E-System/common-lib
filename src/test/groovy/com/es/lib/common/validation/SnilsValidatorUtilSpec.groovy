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
class SnilsValidatorUtilSpec extends Specification {

    def "False for value with length != 11"() {
        expect:
        !SnilsValidatorUtil.isValid(value)
        where:
        value << ["", "1", "12", "123456789", "1234567890", "1234567890123"]
    }

    def "True when null value"() {
        expect:
        SnilsValidatorUtil.isValid(null as String)
    }

    def "False for invalid value"() {
        expect:
        !SnilsValidatorUtil.isValid(value)
        where:
        value << ["11223344596", "08765430301", "08265430200", "08765430311", "1й223344595"]
    }

    def "True for: 11 symbols: 08765430300, 08765430200, 08765430300, 08675430300, 11223344595"() {
        expect:
        SnilsValidatorUtil.isValid(value)
        where:
        value << ["08765430300", "08765430200", "08765430300", "08675430300", "11223344595"]
    }
}
