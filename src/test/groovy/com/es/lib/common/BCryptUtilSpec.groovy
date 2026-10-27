package com.es.lib.common

import spock.lang.Specification

class BCryptUtilSpec extends Specification {

    def "Hash and validate password"() {
        given:
        String password = "secretPassword123"

        when:
        String hash = BCryptUtil.hash(password)

        then:
        hash != null
        BCryptUtil.isValid(password, hash)
        !BCryptUtil.isValid("wrongPassword", hash)
        !BCryptUtil.isValid(null, hash)
        !BCryptUtil.isValid(password, "invalid_hash_format")
    }
}
