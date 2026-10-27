package com.es.lib.common.collection

import spock.lang.Specification

class LimitedQueueSpec extends Specification {

    def "Add elements exceeding limit removes oldest"() {
        given:
        LimitedQueue<String> queue = new LimitedQueue<>(3)

        when:
        queue.add("1")
        queue.add("2")
        queue.add("3")

        then:
        queue.size() == 3
        queue == ["1", "2", "3"]

        when:
        queue.add("4")

        then:
        queue.size() == 3
        queue == ["2", "3", "4"]

        when:
        queue.add("5")
        queue.add("6")

        then:
        queue.size() == 3
        queue == ["4", "5", "6"]
    }
}
