package com.es.lib.common.security.model

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

class AttrsCredentialsSpec extends Specification {

    def "Attr is not null"() {
        expect:
        new AttrsCredentials('login', 'password', null as Map).attrs != null
    }

    def "Attrs filled"() {
        when:
        def host = 'https://localhost'
        def item = new AttrsCredentials('login', 'password', [(AttrsCredentials.ATTR_URI): host])
        then:
        item.attrs[AttrsCredentials.ATTR_URI] == host
    }

    def "Attrs filled from collection"() {
        when:
        def host = 'https://localhost'
        def item = new AttrsCredentials('login', 'password', [Pair.of(AttrsCredentials.ATTR_URI, host)])
        then:
        item.attrs[AttrsCredentials.ATTR_URI] == host
    }
}
