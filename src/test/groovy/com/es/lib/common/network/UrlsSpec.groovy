package com.es.lib.common.network

import spock.lang.Specification

class UrlsSpec extends Specification {

    def "Is valid"() {
        expect:
        Urls.isValid("http://127.0.0.1:80")
        Urls.isValid("https://127.0.0.1:80")
        Urls.isValid("https://127.0.0.1")
        Urls.isValid("https://domain.com:1234")
        Urls.isValid("https://domain.com")
        Urls.isValid(null)
        !Urls.isValid(null, false)
        !Urls.isValid("127.0.0.1:80")
        !Urls.isValid("")
        !Urls.isValid("null")
    }

    def "Is valid list"() {
        expect:
        Urls.isValidList("http://127.0.0.1:80\nhttps://127.0.0.1:80\nhttps://127.0.0.1\nhttps://domain.com:1234\nhttps://domain.com")
        !Urls.isValidList("http://127.0.0.1:80\nhttps://127.0.0.1:80\nhttps://127.0.0.1\nhttps://domain.com:1234\nhttps://domain.com\n127.0.0.1:80")
        !Urls.isValidList("127.0.0.1:80")
        !Urls.isValidList("")
        !Urls.isValidList("null")
    }
}
