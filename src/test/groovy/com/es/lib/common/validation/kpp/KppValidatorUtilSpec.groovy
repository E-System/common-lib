package com.es.lib.common.validation.kpp


import com.es.lib.common.validation.ValidateException
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class KppValidatorUtilSpec extends Specification {

    def "ValidateException for value with length != 9"() {
        when:
        KppValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "Success when null value"() {
        expect:
        KppValidatorUtil.validate(null as String)
    }

    def "BadValueException for invalid value"() {
        when:
        KppValidatorUtil.validate(value)
        then:
        thrown(ValidateException)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122"]
    }

    def "Success for: 9 symbols: 1111AZ122"() {
        when:
        KppValidatorUtil.validate(value)
        then:
        true
        where:
        value << ["1111AZ122", "111111122"]
    }
}
