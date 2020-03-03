package com.es.lib.common.validation.ogrn


import com.es.lib.common.validation.ValidateException
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class OgrnValidatorUtilSpeck extends Specification {

    def "ValidateException for value with length != 13 and length != 15"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.ANY)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "1234567890123456"]
    }

    def "ValidateException for value with length != 13"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.OGRN)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "123456789012345", "1234567890123456"]
    }

    def "ValidateException for value with length != 15"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.OGRNIP)
        then:
        thrown(ValidateException)
        where:
        value << ["", "1", "12345678", "123456789101", "1234567890123", "12345678901234", "1234567890123456"]
    }

    def "Success when null value"() {
        expect:
        OgrnValidatorUtil.validate(null as String, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.validate(null as String, OgrnValidatorUtil.Type.OGRN)
        OgrnValidatorUtil.validate(null as String, OgrnValidatorUtil.Type.OGRNIP)
    }

    def "BadValueException for invalid value"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.ANY)
        then:
        thrown(ValidateException)
        where:
        value << ["5077746887311", "1077746887312", "3й4500116000221"]
    }

    def "Success for: 13 symbols: 5077746887312"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.OGRN)
        then:
        true
        where:
        value << ["5077746887312"]
    }

    def "Success for: 15 symbols: 304500116000221"() {
        when:
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.validate(value, OgrnValidatorUtil.Type.OGRNIP)
        then:
        true
        where:
        value << ["304500116000221"]
    }
}
