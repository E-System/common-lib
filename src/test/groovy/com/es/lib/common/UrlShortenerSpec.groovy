package com.es.lib.common

import spock.lang.Specification

class UrlShortenerSpec extends Specification {

    def "Convert"() {
        when:
        def value = 12345
        def url = UrlShortener.toUrl(value)
        def id = UrlShortener.toValue(url)
        println(url)
        println(id)
        then:
        id == value
    }

    def "Convert zero"() {
        when:
        def value = 0
        def url = UrlShortener.toUrl(value)
        def id = UrlShortener.toValue(url)
        println(url)
        println(id)
        then:
        id == value
        url.isEmpty()
    }

    def "Convert one"() {
        when:
        def value = 1
        def url = UrlShortener.toUrl(value)
        def id = UrlShortener.toValue(url)
        println(url)
        println(id)
        then:
        id == value
    }
}
