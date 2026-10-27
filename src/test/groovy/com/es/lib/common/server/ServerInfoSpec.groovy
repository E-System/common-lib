package com.es.lib.common.server

import spock.lang.Specification

class ServerInfoSpec extends Specification {

    def "Parse server info with slash"() {
        given:
        ServerInfo info = ServerInfo.getInstance()

        when:
        info.init("Apache Tomcat/9.0.41")
        info.log()

        then:
        info.name == "Apache Tomcat"
        info.version == "9.0.41"
        info.isTomcat()
    }

    def "Parse server info without slash"() {
        given:
        ServerInfo info = ServerInfo.getInstance()

        when:
        info.init("Jetty")

        then:
        info.name == "Jetty"
        info.version == null
        !info.isTomcat()
    }

    def "ServerInfoUtil log"() {
        when:
        ServerInfoUtil.log("Apache Tomcat/8.5")
        ServerInfoUtil.log("Undertow")

        then:
        noExceptionThrown()
    }
}
