package com.eslibs.common.configuration.connection

import com.eslibs.common.Jsons
import spock.lang.Specification

class SimpleConnectionSpec extends Specification {

    def "To InetSocketAddress"() {
        when:
        def host = '127.0.0.1'
        def port = 11
        def item = SimpleConnection.builder().host(host).port(port).build()
        def address = item.inetSocketAddress()
        then:
        address.hostString == host
        address.port == port
    }

    def "Serialize to JSON"() {
        when:
        def host = 'https://localhost'
        def port = 11
        def item = SimpleConnection.builder().host(host).port(port).build()
        def json = Jsons.toJson(item)
        def item2 = Jsons.fromJson(json, SimpleConnection.class)
        then:
        noExceptionThrown()
        item.host == item2.host
        item.port == item2.port
    }
}
