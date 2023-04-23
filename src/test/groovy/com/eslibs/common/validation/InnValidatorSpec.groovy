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

package com.eslibs.common.validation

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
class InnValidatorSpec extends Specification {

    def "False for value with length != 10 and length != 12"() {
        expect:
        !Validators.INN.isValid(value)
        where:
        value << ["", "1", "12", "123456789", "12345678901", "1234567890123"]
    }

    def "True when null value"() {
        expect:
        Validators.INN.isValid(null as String)
    }

    def "False for invalid value"() {
        expect:
        !Validators.INN.isValid(value)
        where:
        value << ["1234567890", "1111111111", "123456789012", "111111111111", "7й30002293"]
    }

    def "True for: 12 symbols: 500100732259, 10 symbols: 7830002293"() {
        expect:
        Validators.INN.isValid(value)
        where:
        value << ["500100732259", "7830002293"]
    }
}
