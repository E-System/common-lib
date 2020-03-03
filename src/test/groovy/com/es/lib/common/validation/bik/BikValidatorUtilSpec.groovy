package com.es.lib.common.validation.bik


import com.es.lib.common.validation.ValidateException
import spock.lang.Specification

class BikValidatorUtilSpec extends Specification {

    def "ValidateException for value with length != 9"() {
        when:
        BikValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "Success when null value"() {
        expect:
        BikValidatorUtil.validate(null as String)
    }

    def "BadValueException for invalid value"() {
        when:
        BikValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122", "041212049"]
    }

    def "Success for 9 symbols and in last interval from 050 - 999"() {
        when:
        BikValidatorUtil.validate(value)
        then:
        true
        where:
        value << ["043456789", "041212050"]
    }
}
