package com.es.lib.common.model

import spock.lang.Specification

class RangeSpec extends Specification {

    def "Create empty range"() {
        expect:
        with(Range.empty()) {
            it.from == null
            it.to == null
        }
    }
}
