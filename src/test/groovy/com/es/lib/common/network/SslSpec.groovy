package com.es.lib.common.network

import org.apache.commons.io.IOUtils
import spock.lang.Specification

import javax.net.ssl.X509TrustManager
import java.nio.file.Files
import java.nio.file.Path
import java.security.NoSuchAlgorithmException

class SslSpec extends Specification {

    def "CreateSSLContext"() {
        when:
        def context = Ssl.context()
        then:
        context != null
        context.socketFactory != null
    }

    def "CreateSSLContext with undefined sslType"() {
        when:
        Ssl.context("ABC")
        then:
        def ex = thrown(NoSuchAlgorithmException)
        ex.message == 'ABC SSLContext not available'
    }

    def "Trust manager"() {
        when:
        def manager = (X509TrustManager) Ssl.trustManager()
        then:
        manager.checkClientTrusted(null, null)
        manager.checkServerTrusted(null, null)
        manager.getAcceptedIssuers() == null
    }

    def "Host verifier"() {
        when:
        def verifier = Ssl.allowAllHostVerifier()
        then:
        verifier.verify("any", null)
    }

    def "p12"(){
        when:
        def file = Files.createTempFile("asd", ".p12")
        Files.write(file, IOUtils.toByteArray(Ssl.getResourceAsStream("/com/es/cert/pkcs.p12")))
        println file
        def  keyStore = KeyStore.create(file, "1234567890", "1234567890")
        def context = Ssl.context(keyStore)
        then:
        context != null
        context.socketFactory != null
    }
}
