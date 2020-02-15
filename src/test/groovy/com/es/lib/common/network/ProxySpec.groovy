package com.es.lib.common.network

import spock.lang.Specification

class ProxySpec extends Specification {

    def "To native convert"() {
        when:
        def host = '127.0.0.1'
        def port = 11
        def proxy = new Proxy(host, port).toNative()
        then:
        proxy.type() == java.net.Proxy.Type.HTTP
        ((InetSocketAddress) proxy.address()).hostString == host
        ((InetSocketAddress) proxy.address()).port == port
    }
}
