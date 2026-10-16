package com.es.lib.common.validation.snils

import spock.lang.Specification

class SnilsHelperSpec extends Specification {

    def "Clear"() {
        expect:
        SnilsHelper.clear(null) == null
        SnilsHelper.clear('050-962-648 65') == '05096264865'
        SnilsHelper.clear('050    -962-648     65     ') == '05096264865'
    }

    def "Format"() {
        expect:
        SnilsHelper.format(null) == null
        SnilsHelper.format('05096264865') == '050-962-648 65'
    }
}
