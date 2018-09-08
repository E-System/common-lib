package com.es.lib.common.page

import spock.lang.Shared
import spock.lang.Specification

class PageRequestSpec extends Specification {

    @Shared
    def PAGE_COUNT = 10

    def "Negative page must return first page"() {
        when:
        def request = new PageRequest(PAGE_COUNT, -1)
        then:
        request.offset == 0
        request.limit == PAGE_COUNT
    }

    def "Zero page must return first page"() {
        when:
        def request = new PageRequest(PAGE_COUNT, 0)
        then:
        request.offset == 0
        request.limit == PAGE_COUNT
    }

    def "Zero page count throw IllegalArgument ex"() {
        when:
        new PageRequest(0, 0)
        then:
        thrown(IllegalArgumentException)
    }

    def "Negative page count throw IllegalArgument ex"() {
        when:
        new PageRequest(-1, 0)
        then:
        thrown(IllegalArgumentException)
    }

    def "Fist page"() {
        when:
        def request = new PageRequest(PAGE_COUNT, 1)
        then:
        request.offset == 0
        request.limit == PAGE_COUNT
    }

    def "Second page"(){
        when:
        def request = new PageRequest(PAGE_COUNT, 2)
        then:
        request.offset == PAGE_COUNT
        request.limit == PAGE_COUNT
    }

    def "Default page is first"(){
        when:
        def request = new PageRequest(PAGE_COUNT)
        then:
        request.offset == 0
        request.limit == PAGE_COUNT
    }
}
