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

import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.LocalDate

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
class PassportDateValidatorSpec extends Specification {

    @Shared
    def sdf = new SimpleDateFormat("dd.MM.yyyy")

    def "True for null values"() {
        expect:
        Validators.PASSPORT_DATE.isValid(pd, bd)
        where:
        pd         | bd
        null       | null
        null       | new Date()
        new Date() | null
    }

    def "False passport not changed in 14, 20, 45 years"() {
        expect:
        !Validators.PASSPORT_DATE.isValid(pd, bd)
        where:
        pd                      | bd
        sdf.parse("27.09.1990") | sdf.parse("27.09.1985")
        sdf.parse("27.09.1999") | sdf.parse("27.09.1985")
        sdf.parse("27.09.2000") | sdf.parse("27.09.1985")
        sdf.parse("27.09.2005") | sdf.parse("27.09.1965")
        sdf.parse("27.09.2009") | sdf.parse("27.09.1965")
    }

    def "True if passport changed in 14, 20, 45 years"() {
        expect:
        Validators.PASSPORT_DATE.isValid(pd, bd, LocalDate.of(2015, 9, 26))
        where:
        pd                      | bd
        sdf.parse("27.09.2009") | sdf.parse("27.09.1995")
        sdf.parse("27.09.2005") | sdf.parse("27.09.1985")
        sdf.parse("27.09.2010") | sdf.parse("27.09.1965")
    }
}
