package com.es.lib.common.validation.ogrn

import com.es.lib.common.validation.BadLengthException
import com.es.lib.common.validation.BadValueException
import com.es.lib.common.validation.kpp.KPPValidatorUtil
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class OGRNValidatorUtilSpeck extends Specification {

    def "BadLengthException for value with length != 13 and length != 15"() {
        when:
        OGRNValidatorUtil.validate(value, null)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "1234567890123456"]
    }

    def "BadLengthException for value with length != 13"() {
        when:
        OGRNValidatorUtil.validate(value, 13)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "123456789012345", "1234567890123456"]
    }

    def "BadLengthException for value with length != 15"() {
        when:
        OGRNValidatorUtil.validate(value, 15)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "1234567890123", "12345678901234", "1234567890123456"]
    }

    def "Success when null value"() {
        expect:
        OGRNValidatorUtil.validate(null as String, null)
        OGRNValidatorUtil.validate(null as String, 13)
        OGRNValidatorUtil.validate(null as String, 15)
    }

    def "BadValueException for invalid value"() {
        when:
        OGRNValidatorUtil.validate(value, null)
        then:
        thrown(BadValueException)
        where:
        value << ["5077746887311", "1077746887312", "3й4500116000221"]
    }

    def "Success for: 13 symbols: 5077746887312"() {
        when:
        OGRNValidatorUtil.validate(value, null)
        OGRNValidatorUtil.validate(value, 13)
        then:
        true
        where:
        value << ["5077746887312"]
    }

    def "Success for: 15 symbols: 304500116000221"() {
        when:
        OGRNValidatorUtil.validate(value, null)
        OGRNValidatorUtil.validate(value, 15)
        then:
        true
        where:
        value << ["304500116000221"]
    }
}
