package com.eslibs.common.configuration.credentials

import com.eslibs.common.Jsons
import spock.lang.Specification

class CredentialsSpec extends Specification {

    def builder() {
        return Credentials.builder().login('login').password('password')
    }

    def "Attr is not null"() {
        expect:
        builder().build().attrs != null
    }

    def "Attrs filled"() {
        when:
        def host = 'https://localhost'
        def item = builder().attrs([(Credentials.ATTR_URI): host]).build()
        then:
        item.attrs[Credentials.ATTR_URI] == host
    }

    def "Attrs filled from collection"() {
        when:
        def host = 'https://localhost'
        def item = builder().attrs(Map.of(Credentials.ATTR_URI, host)).build()
        then:
        item.attrs[Credentials.ATTR_URI] == host
    }

    def "Serialize to JSON"() {
        when:
        def host = 'https://localhost'
        def item = builder().attrs(Map.of(Credentials.ATTR_URI, host)).build()
        def json = Jsons.toJson(item)
        def item2 = Jsons.fromJson(json, Credentials.class)
        then:
        noExceptionThrown()
        item.login == item2.login
        item.password == item2.password
        item.attrs == item2.attrs
    }
}
