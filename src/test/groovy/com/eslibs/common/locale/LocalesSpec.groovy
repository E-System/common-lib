package com.eslibs.common.locale

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.01.2018
 */
class LocalesSpec extends Specification {

    def "To locale with null return null"() {
        expect:
        Locales.of(null) == null
    }

    def "To locale with empty string return null"() {
        expect:
        Locales.of("") == null
    }

    def "To locale with invalid code return null"() {
        expect:
        Locales.of("a") == null
    }

    def "To locale with ru_RU code return Locale(ru, RU)"() {
        expect:
        Locales.of("ru_RU") == Locale.of("ru", "RU")
    }

    def "To locale with en_US code return Locale(en, US)"() {
        expect:
        Locales.of("en_US") == Locale.of("en", "US")
    }

    def "To locale with en code return Locale(en, US)"() {
        expect:
        Locales.of("en_US") == Locale.of("en", "US")
        Locales.of("en_US").getUnicodeLocaleType("ca") == null
    }

    def "ToLocale with en code return Locale(th, TH)"() {
        expect:
        Locales.of("th_TH").getUnicodeLocaleType("ca") == "gregory"
    }
}
