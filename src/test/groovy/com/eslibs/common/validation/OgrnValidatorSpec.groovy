/*
 * Copyright 2020 E-System LLC
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
 * @since 25.07.16
 */
class OgrnValidatorSpec extends Specification {

    def "False for value with length != 13 and length != 15"() {
        expect:
        !Validators.OGRN.isValid(value, OgrnType.ANY)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "1234567890123456"]
    }

    def "False for value with length != 13"() {
        expect:
        !Validators.OGRN.isValid(value, OgrnType.OGRN)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "123456789012345", "1234567890123456"]
    }

    def "False for value with length != 15"() {
        expect:
        !Validators.OGRN.isValid(value, OgrnType.OGRNIP)
        where:
        value << ["", "1", "12345678", "123456789101", "1234567890123", "12345678901234", "1234567890123456"]
    }

    def "True when null value"() {
        expect:
        Validators.OGRN.isValid(null as String, OgrnType.ANY)
        Validators.OGRN.isValid(null as String, OgrnType.OGRN)
        Validators.OGRN.isValid(null as String, OgrnType.OGRNIP)
    }

    def "False for invalid value"() {
        expect:
        !Validators.OGRN.isValid(value, OgrnType.ANY)
        where:
        value << ["5077746887311", "1077746887312", "3й4500116000221"]
    }

    def "True for: 13 symbols: 5077746887312"() {
        expect:
        Validators.OGRN.isValid(value, OgrnType.ANY)
        Validators.OGRN.isValid(value, OgrnType.OGRN)
        where:
        value << ["5077746887312"]
    }

    def "True for: 15 symbols: 304500116000221"() {
        expect:
        Validators.OGRN.isValid(value, OgrnType.ANY)
        Validators.OGRN.isValid(value, OgrnType.OGRNIP)
        where:
        value << ["304500116000221"]
    }
}
