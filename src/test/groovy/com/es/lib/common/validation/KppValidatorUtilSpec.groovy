package com.es.lib.common.validation

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class KppValidatorUtilSpec extends Specification {

    def "False for value with length != 9"() {
        expect:
        !KppValidatorUtil.isValid(value)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "True when null value"() {
        expect:
        KppValidatorUtil.isValid(null as String)
    }

    def "False for invalid value"() {
        expect:
        !KppValidatorUtil.isValid(value)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122"]
    }

    def "True for: 9 symbols: 1111AZ122"() {
        expect:
        KppValidatorUtil.isValid(value)
        where:
        value << ["1111AZ122", "111111122"]
    }
}
