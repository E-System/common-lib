package com.es.lib.common.validation

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
class OgrnValidatorUtilSpeck extends Specification {

    def "False for value with length != 13 and length != 15"() {
        expect:
        !OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.ANY)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "1234567890123456"]
    }

    def "False for value with length != 13"() {
        expect:
        !OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.OGRN)
        where:
        value << ["", "1", "12345678", "123456789101", "12345678901234", "123456789012345", "1234567890123456"]
    }

    def "False for value with length != 15"() {
        expect:
        !OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.OGRNIP)
        where:
        value << ["", "1", "12345678", "123456789101", "1234567890123", "12345678901234", "1234567890123456"]
    }

    def "True when null value"() {
        expect:
        OgrnValidatorUtil.isValid(null as String, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.isValid(null as String, OgrnValidatorUtil.Type.OGRN)
        OgrnValidatorUtil.isValid(null as String, OgrnValidatorUtil.Type.OGRNIP)
    }

    def "False for invalid value"() {
        expect:
        !OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.ANY)
        where:
        value << ["5077746887311", "1077746887312", "3й4500116000221"]
    }

    def "True for: 13 symbols: 5077746887312"() {
        expect:
        OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.OGRN)
        where:
        value << ["5077746887312"]
    }

    def "True for: 15 symbols: 304500116000221"() {
        expect:
        OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.ANY)
        OgrnValidatorUtil.isValid(value, OgrnValidatorUtil.Type.OGRNIP)
        where:
        value << ["304500116000221"]
    }
}
