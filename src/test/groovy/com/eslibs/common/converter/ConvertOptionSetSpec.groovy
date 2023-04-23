package com.eslibs.common.converter

import spock.lang.Specification

class ConvertOptionSetSpec extends Specification {

    enum SimpleOption implements ConvertOption {
        SHORT,
        FULL
    }

    class TestOption implements ConvertOption {
        String value

        TestOption(String value) {
            this.value = value
        }
    }

    def "Create with collection"() {
        when:
        def items = [SimpleOption.SHORT, new TestOption('Hello')]
        def opt = new ConvertOption.Set(items)
        then:
        opt.size() == 2
        opt[0] == SimpleOption.SHORT
        (opt[1] as TestOption).value == 'Hello'
    }

    def "Create with collection and args"() {
        when:
        def items = [SimpleOption.SHORT, new TestOption('Hello')]
        def opt = new ConvertOption.Set(items, SimpleOption.FULL)
        then:
        opt.size() == 3
        opt[0] == SimpleOption.SHORT
        (opt[1] as TestOption).value == 'Hello'
        opt[2] == SimpleOption.FULL
    }
}
