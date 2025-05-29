package com.es.lib.common.client

import spock.lang.Specification

import java.time.ZoneId

class ClientInfoSpec extends Specification {

    def "Create"() {
        when:
        def platform = 'iOS'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'    : platform,
                'es-platform-version': platformVersion,
                'es-app-version'     : appVersion
        ]
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.ios
        clientInfo.platformVersion == platformVersion
        clientInfo.appVersion == appVersion
    }

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
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.ios
        clientInfo.platformVersion == platformVersion
        clientInfo.appVersion == appVersion
    }

    def "Create with new headers all"() {
        when:
        def platform = 'iOS'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def guid = UUID.randomUUID().toString()
        def headers = [
                'es-app-platform'        : platform,
                'es-app-platform-version': platformVersion,
                'es-app-version'         : appVersion,
                'es-app-timezone'        : 'GMT+03',
                'es-app-locale'          : 'ru_RU',
                'es-app-key'             : guid
        ]
        def result = ClientInfo.create(headers)
        then:
        result.platform == ClientInfo.Platform.ios
        result.platformVersion == platformVersion
        result.appVersion == appVersion
        result.appLocale == Locale.default
        result.appTimezone == ZoneId.of('GMT+03')
        result.appKey == guid
    }


    def "Create with empty headers"() {
        when:
        def headers = [:] as Map
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.undefined
        clientInfo.platformVersion == ''
        clientInfo.appVersion == ''
    }

    def "Create with invalid platform"() {
        when:
        def platform = 'qwe'
        def platformVersion = 'iOS 13.0'
        def appVersion = '1.0.0'
        def headers = [
                'es-app-platform'    : platform,
                'es-platform-version': platformVersion,
                'es-app-version'     : appVersion
        ]
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.undefined
        clientInfo.platformVersion == platformVersion
        clientInfo.appVersion == appVersion
    }
}
