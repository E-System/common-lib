package com.es.lib.common.validation

import spock.lang.Specification

class RangeValidatorUtilSpec extends Specification {

    def "True when null value with any pattern"() {
        expect:
        RangeValidatorUtil.isValid(null as String, null as String)
        RangeValidatorUtil.isValid(null as String, "")
    }

    def "False with invalid value"() {
        expect:
        !RangeValidatorUtil.isValid("Hello", "\\d")
    }

    def "True for valid values"() {
        expect:
        RangeValidatorUtil.isValid("123", "[123;124]")
        RangeValidatorUtil.isValid("124", "[123;124]")
    }

    def "True multiple pattern"() {
        expect:
        RangeValidatorUtil.isValid("130", "(123;124)[130;131]")
        RangeValidatorUtil.isValid("132", "(123;124)(130;132]")
    }

    def "False with border"() {
        expect:
        !RangeValidatorUtil.isValid("123", "(123;124]")
        !RangeValidatorUtil.isValid("123", "(123;124)")
    }
}
