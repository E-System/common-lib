package com.es.lib.common.security.model

import com.es.lib.common.JsonUtil
import spock.lang.Specification

class CredentialsSpec extends Specification {

    def "Serialize and deserialize"() {
        when:
        def item = new Credentials('LOGIN', 'PASSWORD')
        def json = JsonUtil.toJson(item)
        def item2 = JsonUtil.fromJson(json, Credentials)
        then:
        item.login == item2.login
        item.password == item2.password
        item == item2
    }
}
