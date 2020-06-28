package com.es.lib.common.client

import com.es.lib.common.model.ClientInfo
import spock.lang.Specification

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
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.ios
        clientInfo.platformVersion == platformVersion
        clientInfo.appVersion == appVersion
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
            'es-app-platform-version': platformVersion,
            'es-app-version'     : appVersion
        ]
        def clientInfo = ClientInfo.create(headers)
        then:
        clientInfo.platform == ClientInfo.Platform.undefined
        clientInfo.platformVersion == platformVersion
        clientInfo.appVersion == appVersion
    }
}
