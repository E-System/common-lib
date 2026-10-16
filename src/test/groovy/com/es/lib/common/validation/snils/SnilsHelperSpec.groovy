package com.es.lib.common.validation.snils

import spock.lang.Specification

class SnilsHelperSpec extends Specification {

    def "clear"() {
        expect:
        SnilsHelper.clear(snils) == result
        where:
        snils                         || result
        null                          || null
        ''                            || ''
        '050-962-648 65'              || '05096264865'
        '050    -962-648     65     ' || '05096264865'
        '123-123-123 11'              || '12312312311'
    }

    def "format"() {
        expect:
        SnilsHelper.format(snils) == result
        where:
        snils            || result
        null             || null
        ''               || ''
        '12312312311'    || '123-123-123 11'
        '123-123-123 11' || '123-123-123 11'
        '05096264865'    || '050-962-648 65'
    }
}
