package com.es.lib.common.locale

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.01.2018
 */
class LocaleUtilSpec extends Specification {

    def "ToLocale with null return null"() {
        expect:
        LocaleUtil.toLocale(null) == null
    }

    def "ToLocale with empty string return null"() {
        expect:
        LocaleUtil.toLocale("") == null
    }

    def "ToLocale with invalid code return null"() {
        expect:
        LocaleUtil.toLocale("a") == null
    }

    def "ToLocale with ru_RU code return Locale(ru, RU)"() {
        expect:
        LocaleUtil.toLocale("ru_RU") == new Locale("ru", "RU")
    }

    def "ToLocale with en_US code return Locale(en, US)"() {
        expect:
        LocaleUtil.toLocale("en_US") == new Locale("en", "US")
    }

    def "ToLocale with en code return Locale(en, US)"() {
        expect:
        LocaleUtil.toLocale("en_US") == new Locale("en", "US")
    }
}
