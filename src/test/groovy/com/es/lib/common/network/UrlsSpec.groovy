package com.es.lib.common.network

import spock.lang.Specification

class UrlsSpec extends Specification {

    def "Replace host"(){
        expect:
        Urls.replaceHost(null, 'local.host') == null
        Urls.replaceHost('','local.host') == null
        Urls.replaceHost('https://127.0.0.1:80','local.host') == 'https://local.host:80'
        Urls.replaceHost('127.0.0.1','192.168.0.1') == '192.168.0.1'
        Urls.replaceHost('127.0.0.1:80','192.168.0.1') == '192.168.0.1:80'
        Urls.replaceHost('https://domain.com','local.host') == 'https://local.host'
        Urls.replaceHost('https://domain.com:443','local.host') == 'https://local.host:443'
    }

    def "Host"(){
        expect:
        Urls.host(null) == null
        Urls.host('') == null
        Urls.host('127.0.0.1') == '127.0.0.1'
        Urls.host('127.0.0.1:80') == '127.0.0.1'
        Urls.host('https://127.0.0.1:80') == '127.0.0.1'
        Urls.host('https://domain.com') == 'domain.com'
        Urls.host('https://domain.com:443') == 'domain.com'
    }
    def "AsString"() {
        expect:
        Urls.asString(Urls.split(null)) == null
        Urls.asString(Urls.split("")) == null
        Urls.asString(Urls.split("https://127.0.0.1:80")) == 'https://127.0.0.1:80'
        Urls.asString(Urls.split("http://127.0.0.1:80\nhttps://127.0.0.1:80    \nhttps://127.0.0.1\n    https://domain.com:1234\nhttps://domain.com")) == 'http://127.0.0.1:80\nhttps://127.0.0.1:80\nhttps://127.0.0.1\nhttps://domain.com:1234\nhttps://domain.com'
    }

    def "Merge"(){
        expect:
        Urls.merge(null, '') == null
        Urls.merge(null, null) == null
        Urls.merge(null, '127.0.0.1') == '127.0.0.1'
        Urls.merge(['127.0.0.1'], 'http://localhost:80') == 'http://localhost:80\n127.0.0.1'
    }

    def "Split"() {
        expect:
        Urls.split(null) == []
        Urls.split("") == []
        with(Urls.split("https://127.0.0.1:80")) {
            it.size() == 1
        }
        with(Urls.split("http://127.0.0.1:80\nhttps://127.0.0.1:80    \nhttps://127.0.0.1\n    https://domain.com:1234\nhttps://domain.com")) {
            it.size() == 5
            it[0] == 'http://127.0.0.1:80'
            it[1] == 'https://127.0.0.1:80'
            it[2] == 'https://127.0.0.1'
            it[3] == 'https://domain.com:1234'
            it[4] == 'https://domain.com'
        }
        with(Urls.split("http://127.0.0.1:80\nhttps://127.0.0.1:80    \nhttps://127.0.0.1\n    https://domain.com:1234\nhttps://domain.com", "/core-api")) {
            it.size() == 5
            it[0] == 'http://127.0.0.1:80/core-api'
            it[1] == 'https://127.0.0.1:80/core-api'
            it[2] == 'https://127.0.0.1/core-api'
            it[3] == 'https://domain.com:1234/core-api'
            it[4] == 'https://domain.com/core-api'
        }
    }

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
        Urls.isValidList("")
        Urls.isValidList(null)
        Urls.isValidList("http://127.0.0.1:80\nhttps://127.0.0.1:80\nhttps://127.0.0.1\nhttps://domain.com:1234\nhttps://domain.com")
        !Urls.isValidList("http://127.0.0.1:80\nhttps://127.0.0.1:80\nhttps://127.0.0.1\nhttps://domain.com:1234\nhttps://domain.com\n127.0.0.1:80")
        !Urls.isValidList("127.0.0.1:80")
        !Urls.isValidList("null")
    }
}
