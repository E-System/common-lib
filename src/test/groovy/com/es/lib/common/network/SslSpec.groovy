package com.es.lib.common.network

import spock.lang.Specification

import java.security.NoSuchAlgorithmException

class SslSpec extends Specification {

    def "CreateSSLContext"() {
        expect:
        Ssl.context() != null
    }

    def "CreateSSLContext with undefined sslType"() {
        when:
        Ssl.context("ABC")
        then:
        def ex = thrown(NoSuchAlgorithmException)
        ex.message == 'ABC SSLContext not available'
    }
}
