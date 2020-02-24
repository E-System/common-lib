package com.es.lib.common.validation.range

import com.es.lib.common.validation.BadValueException
import spock.lang.Specification

class RangeValidatorUtilSpec extends Specification {

    def "Success when null value with any pattern"() {
        expect:
        RangeValidatorUtil.validate(null as String, null as String)
        RangeValidatorUtil.validate(null as String, "")
    }

    def "Exception with invalid value"() {
        when:
        RangeValidatorUtil.validate("Hello", "\\d")
        then:
        thrown(BadValueException)
    }

    def "Success pattern"() {
        expect:
        RangeValidatorUtil.validate("123", "[123;124]")
        RangeValidatorUtil.validate("124", "[123;124]")
    }


    def "Success multiple pattern"() {
        expect:
        RangeValidatorUtil.validate("130", "(123;124)[130;131]")
        RangeValidatorUtil.validate("132", "(123;124)(130;132]")
    }

    def "Invalid border"() {
        when:
        RangeValidatorUtil.validate("123", "(123;124]")
        RangeValidatorUtil.validate("123", "(123;124)")
        then:
        thrown(BadValueException)
    }
}
