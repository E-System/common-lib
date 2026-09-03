package com.es.lib.common.file

import spock.lang.Specification

class DiscInfoSpec extends Specification {

    def "Create"() {
        expect:
        with(DiscInfo.create()) {
            println(it)
            it.total != 0
            it.free != 0
        }
    }
}
