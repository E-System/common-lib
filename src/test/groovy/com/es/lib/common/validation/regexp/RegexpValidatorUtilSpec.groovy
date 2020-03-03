package com.es.lib.common.validation.regexp

import com.es.lib.common.validation.ValidateException
import spock.lang.Specification

class RegexpValidatorUtilSpec extends Specification {

    def "Success when null value with any pattern"() {
        expect:
        RegexpValidatorUtil.validate(null as String, null as String)
        RegexpValidatorUtil.validate(null as String, "")
        RegexpValidatorUtil.validate(null as String, "123")
    }

    def "Exception with invalid value"() {
        when:
        RegexpValidatorUtil.validate("Hello", "\\d")
        then:
        thrown(ValidateException)
    }

    def "Success pattern"() {
        expect:
        RegexpValidatorUtil.validate("123", "\\d*")
    }
}
