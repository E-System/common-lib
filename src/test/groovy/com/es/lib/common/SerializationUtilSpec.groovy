package com.es.lib.common

import spock.lang.Specification

class SerializationUtilSpec extends Specification {

    def "RemoveDelimiters"() {
        expect:
        SerializationUtil.removeDelimiters(value) == result
        where:
        value          | result
        'г.,'          | 'г'
        'г,'           | 'г'
        'г'            | 'г'
        ''             | ''
        'г. Барнаул'   | 'г Барнаул'
        ',г. Барнаул.' | 'г Барнаул'
    }

    def "ReplaceDelimiters"() {
        expect:
        SerializationUtil.replaceDelimiters(value, target) == result
        where:
        value         | target | result
        'г.,'         | '|'    | 'г||'
        'г,'          | '|'    | 'г|'
        'г'           | '|'    | 'г'
        ''            | '|'    | ''
        'г. Барнаул'  | '|'    | 'г| Барнаул'
        ',г.Барнаул.' | '|'    | '|г|Барнаул|'
    }
}
