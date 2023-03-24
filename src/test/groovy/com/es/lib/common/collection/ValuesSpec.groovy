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
}
