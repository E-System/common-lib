package com.eslibs.common.security

import spock.lang.Specification

import javax.net.ssl.X509TrustManager
import java.security.NoSuchAlgorithmException

class SslSpec extends Specification {

    def "CreateSSLContext with allTrust"() {
        when:
        def context = Ssl.context()
        then:
        context != null
        context.socketFactory != null
    }

    def "CreateSSLContext without allTrust"() {
        when:
        def context = Ssl.context(false)
        then:
        context != null
        context.socketFactory != null
    }

    def "CreateSSLContext with undefined sslType"() {
        when:
        Ssl.context(null as Ssl.Mode)
        then:
        def ex = thrown(NullPointerException)
        ex.message == 'null protocol name'
    }

    def "Trust manager"() {
        when:
        def manager = (X509TrustManager) Ssl.trustManager()
        then:
        manager.checkClientTrusted(null, null)
        manager.checkServerTrusted(null, null)
        manager.getAcceptedIssuers() != null
    }

    def "Host verifier"() {
        when:
        def verifier = Ssl.allowAllHostVerifier()
        then:
        verifier.verify("any", null)
    }
}
