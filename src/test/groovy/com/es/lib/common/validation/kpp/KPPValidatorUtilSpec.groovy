package com.es.lib.common.validation.kpp

import com.es.lib.common.validation.BadLengthException
import com.es.lib.common.validation.BadValueException
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class KPPValidatorUtilSpec extends Specification {

    def "BadLengthException for value with length != 9"() {
        when:
        KPPValidatorUtil.validate(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "12345678910"]
    }

    def "Success when null value"() {
        expect:
        KPPValidatorUtil.validate(null as String)
    }

    def "BadValueException for invalid value"() {
        when:
        KPPValidatorUtil.validate(value)
        then:
        thrown(BadValueException)
        where:
        value << ["AAAA12123", "AAAA12BBB", "AAAAAAAAA", "фывапролд", "1111az122", "1й1111122"]
    }

    def "Success for: 9 symbols: 1111AZ122"() {
        when:
        KPPValidatorUtil.validate(value)
        then:
        true
        where:
        value << ["1111AZ122", "111111122"]
    }
}
