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
        OGRNValidatorUtil.validate(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "1234567890123456"]
    }

    def "BadLengthException for value with length != 13"() {
        when:
        OGRNValidatorUtil.validate13(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "123456789012345", "1234567890123456"]
    }

    def "BadLengthException for value with length != 15"() {
        when:
        OGRNValidatorUtil.validate15(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["", "1", "12345678", "123456789101", "1234567890123", "12345678901234", "1234567890123456"]
    }

    def "BadLengthException when null value"() {
        when:
        OGRNValidatorUtil.validate(null as String)
        OGRNValidatorUtil.validate13(null as String)
        OGRNValidatorUtil.validate15(null as String)
        then:
        thrown(BadLengthException)
    }

    def "BadValueException for invalid value"() {
        when:
        OGRNValidatorUtil.validate(value)
        then:
        thrown(BadValueException)
        where:
        value << ["5077746887311", "1077746887312", "3й4500116000221"]
    }

    def "Success for: 13 symbols: 5077746887312"() {
        when:
        OGRNValidatorUtil.validate(value)
        OGRNValidatorUtil.validate13(value)
        then:
        true
        where:
        value << ["5077746887312"]
    }

    def "Success for: 15 symbols: 304500116000221"() {
        when:
        OGRNValidatorUtil.validate(value)
        OGRNValidatorUtil.validate15(value)
        then:
        true
        where:
        value << ["304500116000221"]
    }
}
