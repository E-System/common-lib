package com.es.lib.common.validation.bik

import com.es.lib.common.validation.BadLengthException
import com.es.lib.common.validation.BadValueException
import spock.lang.Specification

class BikValidatorUtilSpec extends Specification {

    def "BadLengthException for value with length != 9"() {
        when:
        BikValidatorUtil.validate(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "BadLengthException when null value"() {
        when:
        BikValidatorUtil.validate(null as String)
        then:
        thrown(BadLengthException)
    }

    def "BadValueException for invalid value"() {
        when:
        BikValidatorUtil.validate(value)
        then:
        thrown(BadValueException)
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
