package com.eslibs.common.model

import com.eslibs.common.locale.Locales
import spock.lang.Specification

import java.time.ZoneId

class ClientInfoSpec extends Specification {

    def "Create with new headers"() {
        when:
        def platform = 'iOS'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'        : platform,
                'es-app-platform-version': platformVersion,
                'es-app-version'         : appVersion
        ]
        def result = ClientInfo.from(headers)
        then:
        result.platform() == ClientInfo.Platform.ios
        result.platformVersion() == platformVersion
        result.appVersion() == appVersion
        result.appTimezone() == ZoneId.systemDefault()
        result.appLocale() == Locale.default
    }

    def "Create with full headers"() {
        when:
        def platform = 'iOS'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'        : platform,
                'es-app-platform-version': platformVersion,
                'es-app-version'         : appVersion,
                'es-app-timezone'        : "GMT+03",
                'es-app-locale'          : "en_US"
        ]
        def result = ClientInfo.from(headers)
        then:
        result.platform() == ClientInfo.Platform.ios
        result.platformVersion() == platformVersion
        result.appVersion() == appVersion
        result.appTimezone() == ZoneId.of("GMT+03")
        result.appLocale() == Locales.of("en_US")
    }

    def "Create with full headers (2)"() {
        when:
        def platform = 'iOS'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'        : platform,
                'es-app-platform-version': platformVersion,
                'es-app-version'         : appVersion,
                'es-app-timezone'        : "Europe/Moscow",
                'es-app-locale'          : "en_US"
        ]
        def result = ClientInfo.from(headers)
        then:
        result.platform() == ClientInfo.Platform.ios
        result.platformVersion() == platformVersion
        result.appVersion() == appVersion
        result.appTimezone() == ZoneId.of("Europe/Moscow")
        result.appLocale() == Locales.of("en_US")
    }

    def "Create with empty headers"() {
        when:
        def headers = [:] as Map
        def result = ClientInfo.from(headers)
        then:
        result.platform() == ClientInfo.Platform.undefined
        result.platformVersion() == ''
        result.appVersion() == ''
        result.appTimezone() == ZoneId.systemDefault()
        result.appLocale() == Locale.default
    }

    def "Create with invalid platform"() {
        when:
        def platform = 'qwe'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'        : platform,
                'es-app-platform-version': platformVersion,
                'es-app-version'         : appVersion
        ]
        def result = ClientInfo.from(headers)
        then:
        result.platform() == ClientInfo.Platform.undefined
        result.platformVersion() == platformVersion
        result.appVersion() == appVersion
        result.appTimezone() == ZoneId.systemDefault()
        result.appLocale() == Locale.default
    }
}
