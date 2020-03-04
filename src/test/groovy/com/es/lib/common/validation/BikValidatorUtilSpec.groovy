package com.es.lib.common.validation

import spock.lang.Specification

class BikValidatorUtilSpec extends Specification {

    def "False for value with length != 9"() {
        expect:
        !BikValidatorUtil.isValid(value)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "True when null value"() {
        expect:
        BikValidatorUtil.isValid(null as String)
    }

    def "False for invalid value"() {
        expect:
        !BikValidatorUtil.isValid(value)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122", "041212049"]
    }

    def "True for 9 symbols and in last interval from 050 - 999"() {
        expect:
        BikValidatorUtil.isValid(value)
        where:
        value << ["043456789", "041212050"]
    }
}
