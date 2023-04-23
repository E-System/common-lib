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

class RangeValidatorSpec extends Specification {

    def "True when null value with any pattern"() {
        expect:
        Validators.RANGE.isValid(null as String, null as String)
        Validators.RANGE.isValid(null as String, "")
    }

    def "False with invalid value"() {
        expect:
        !Validators.RANGE.isValid("Hello", "\\d")
    }

    def "True for valid values"() {
        expect:
        Validators.RANGE.isValid("123", "[123;124]")
        Validators.RANGE.isValid("124", "[123;124]")
    }

    def "True multiple pattern"() {
        expect:
        Validators.RANGE.isValid("130", "(123;124)[130;131]")
        Validators.RANGE.isValid("132", "(123;124)(130;132]")
    }

    def "False with border"() {
        expect:
        !Validators.RANGE.isValid("123", "(123;124]")
        !Validators.RANGE.isValid("123", "(123;124)")
    }
}
