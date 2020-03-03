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

package com.es.lib.common.validation.snils


import com.es.lib.common.validation.ValidateException
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
class SnilsValidatorUtilSpec extends Specification {

    def "ValidateException for value with length != 11"() {
        when:
        SnilsValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12", "123456789", "1234567890", "1234567890123"]
    }

    def "Success when null value"() {
        expect:
        SnilsValidatorUtil.validate(null as String)
    }

    def "BadValueException for invalid value"() {
        when:
        SnilsValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["11223344596", "08765430301", "08265430200", "08765430311", "1й223344595"]
    }

    def "Success for: 11 symbols: 08765430300, 08765430200, 08765430300, 08675430300, 11223344595"() {
        when:
        SnilsValidatorUtil.validate(value)
        then:
        true
        where:
        value << ["08765430300", "08765430200", "08765430300", "08675430300", "11223344595"]
    }
}
