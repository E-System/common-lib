package com.eslibs.common.configuration

import com.eslibs.common.configuration.connection.SimpleConnection
import spock.lang.Specification

class ProxySpec extends Specification {

    def "To native"() {
        when:
        def host = '127.0.0.1'
        def port = 11
        def item = Proxy.builder().connection(
                SimpleConnection.builder().host(host).port(port).build()
        ).build()
        def proxy = item.proxy()
        then:
        proxy.type() == java.net.Proxy.Type.HTTP
        with(proxy.address() as InetSocketAddress) {
            it.hostString == host
            it.port == port
        }
    }
}
