package com.es.lib.common.security.model

import com.es.lib.common.JsonUtil
import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

class AttrsCredentialsSpec extends Specification {

   /* def "Attr is not null"() {
        expect:
        new AttrsCredentials('login', 'password', null as Collection).attrs != null
    }*/

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

    def "Serialize and deserialize"() {
        when:
        def item = new AttrsCredentials('LOGIN', 'PASSWORD', [(AttrsCredentials.ATTR_URI): "https://localhost"])
        def json = JsonUtil.toJson(item)
        def item2 = JsonUtil.fromJson(json, AttrsCredentials)
        then:
        item.login == item2.login
        item.password == item2.password
        item.attrs == item2.attrs
    }
}
