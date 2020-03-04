package com.es.lib.common.validation

import spock.lang.Specification

class RegexpValidatorUtilSpec extends Specification {

    def "True when null value with any pattern"() {
        expect:
        RegexpValidatorUtil.isValid(null as String, null as String)
        RegexpValidatorUtil.isValid(null as String, "")
        RegexpValidatorUtil.isValid(null as String, "123")
    }

    def "False with invalid value"() {
        expect:
        !RegexpValidatorUtil.isValid("Hello", "\\d")
    }

    def "True with correct pattern"() {
        expect:
        RegexpValidatorUtil.isValid("123", "\\d*")
    }
}
