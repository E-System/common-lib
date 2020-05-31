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
package com.es.lib.common.validation

import spock.lang.Specification

class BikValidatorSpec extends Specification {

    def "False for value with length != 9"() {
        expect:
        !Validators.BIK.isValid(value)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "True when null value"() {
        expect:
        Validators.BIK.isValid(null as String)
    }

    def "False for invalid value"() {
        expect:
        !Validators.BIK.isValid(value)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122", "041212049"]
    }

    def "True for 9 symbols and in last interval from 050 - 999"() {
        expect:
        Validators.BIK.isValid(value)
        where:
        value << ["043456789", "041212050"]
    }
}
