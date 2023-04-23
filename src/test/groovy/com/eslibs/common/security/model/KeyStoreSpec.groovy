package com.eslibs.common.security.model

import spock.lang.Specification

import java.nio.file.Paths

class KeyStoreSpec extends Specification {

    def "Key type by extension"() {
        expect:
        KeyStore.create(Paths.get("/tmp/test.jks"), '', '').type == 'JKS'
        KeyStore.create(Paths.get("/tmp/test.p12"), '', '').type == 'PKCS12'
        KeyStore.create(Paths.get("/tmp/test.pfx"), '', '').type == 'PKCS12'
    }

    def "Invalid key type by extension"() {
        when:
        KeyStore.create(Paths.get("/tmp/test.jks1"), '', '')
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == 'Unable to determine key type by file: /tmp/test.jks1'
    }

    def "Invalid key type by empty extension"() {
        when:
        KeyStore.create(Paths.get("/tmp/test"), '', '')
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == 'Unable to determine key type by file: /tmp/test'
    }

    def "Create with type"() {
        expect:
        KeyStore.create(Paths.get("/tmp/test.jks"), '', '', 'PKCS12').type == 'PKCS12'
    }
}
