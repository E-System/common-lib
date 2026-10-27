package com.es.lib.common

import spock.lang.Specification

class ThreadUtilSpec extends Specification {

    def "Sleep executes without throwing"() {
        when:
        long start = System.currentTimeMillis()
        ThreadUtil.sleep(50)
        long elapsed = System.currentTimeMillis() - start

        then:
        elapsed >= 40
    }
}
