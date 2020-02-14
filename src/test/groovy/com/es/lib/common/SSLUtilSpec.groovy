package com.es.lib.common

import spock.lang.Specification

import java.security.NoSuchAlgorithmException

class SSLUtilSpec extends Specification {

    def "CreateSSLContext"() {
        expect:
        SSLUtil.createSSLContext() != null
    }

    def "CreateSSLContext with undefined sslType"() {
        when:
        SSLUtil.createSSLContext("ABC")
        then:
        def ex = thrown(NoSuchAlgorithmException)
        ex.message == 'ABC SSLContext not available'
    }
}
