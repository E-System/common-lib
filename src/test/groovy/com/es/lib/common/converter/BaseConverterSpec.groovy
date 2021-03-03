package com.es.lib.common.converter

import spock.lang.Specification

class BaseConverterSpec extends Specification {

    class Input {
        String value

        Input(String value) {
            this.value = value
        }
    }

    class Output {
        String value

        Output(String value) {
            this.value = value
        }

    }

    enum SimpleOption implements ConvertOption {
        SHORT,
        FULL
    }

    class IOConverter extends BaseConverter<Output, Input> {

        @Override
        protected Output realConvert(Input item, Set<ConvertOption> options) {
            if (options.contains(SimpleOption.FULL)) {
                return new Output(item.value + "FULL")
            }
            return new Output(item.value)
        }
    }

    def "Null item return null"() {
        when:
        def converter = new IOConverter()
        then:
        converter.convert((Input) null) == null
    }

    def "Not null items return not null"() {
        when:
        def converter = new IOConverter()
        def res = converter.convert(new Input("Hello"))
        then:
        res != null
        res.value == "Hello"
    }

    def "Not null items return not null with options"() {
        when:
        def converter = new IOConverter()
        def res = converter.convert(new Input("Hello"), SimpleOption.FULL)
        then:
        res != null
        res.value == "HelloFULL"
    }

    def "Result exclude null items"() {
        when:
        def converter = new IOConverter()
        def res = converter.convert([null, new Input("Hello"), null])
        then:
        res != null
        res.size() == 1
        res[0].value == "Hello"
    }

    def "On empty or null collection return empty collection"() {
        when:
        def converter = new IOConverter()
        def res = converter.convert(null as List)
        then:
        res != null
        res.size() == 0
    }
}
