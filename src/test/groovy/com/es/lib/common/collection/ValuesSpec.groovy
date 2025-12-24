package com.es.lib.common.collection

import spock.lang.Specification

class ValuesSpec extends Specification {

    def "IsEmpty"() {
        expect:
        Values.of(null).empty
        Values.of('').empty
        !Values.of('1').empty
        Values.of(null, null).empty
        Values.of('', '').empty
        !Values.of('1', '').empty
        !Values.of('', '2').empty
        !Values.of('1', '2').empty
    }

    def "Get"() {
        expect:
        Values.of(null).get(0) == null
        Values.of('1').get(0) == '1'
        when:
        Values.of('1').get(1)
        then:
        thrown(ArrayIndexOutOfBoundsException)
    }

    def "Get boolean"() {
        expect:
        Values.of(null).get(0, Boolean::parseBoolean) == null
        Values.of("true").get(0, Boolean::parseBoolean)
        !Values.of("false").get(0, Boolean::parseBoolean)
        Values.of(null).get(0, Boolean::parseBoolean, true)
    }

    def "Get integer"() {
        expect:
        Values.of(null).get(0, Integer::parseInt) == null
        Values.of("1").get(0, Integer::parseInt) == 1
        Values.of("2").get(0, Integer::parseInt) == 2
        Values.of(null).get(0, Integer::parseInt, 10) == 10
    }
}
