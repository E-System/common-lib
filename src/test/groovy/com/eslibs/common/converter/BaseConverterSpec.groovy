package com.eslibs.common.converter

import com.eslibs.common.converter.option.IncludeConvertOption
import com.eslibs.common.converter.option.LocaleConvertOption
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

    class TestOption implements ConvertOption {
        String value

        TestOption(String value) {
            this.value = value
        }
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

    class LocaleIOConverter extends BaseConverter<Output, Input> {

        @Override
        protected Output realConvert(Input item, Set<ConvertOption> options) {
            def opt = getLocaleOption(options)
            return new Output(item.value + '[' + opt + ']' + getOption(options, TestOption).orElse(new TestOption('')).value)
        }
    }

    class IncludeIOConverter extends BaseConverter<Output, Input> {
        @Override
        protected Output realConvert(Input item, Set<ConvertOption> options) {
            def opt = getIncludeOption(options)
            def result = new Output(item.value)
            opt.process(item, result) { field, from, to ->
                to.value += ('[' + field + ']')
            }
            return result
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

    def "LocaleConvertOption exist"() {
        when:
        def converter = new LocaleIOConverter()
        def res = converter.convert(new Input("Hello"), new LocaleConvertOption("ru_RU"))
        then:
        res != null
        res.value == 'Hello[ru_RU]'
    }

    def "LocaleConvertOption exist and TestOption"() {
        when:
        def converter = new LocaleIOConverter()
        def res = converter.convert(new Input("Hello"), new LocaleConvertOption("ru_RU"), new TestOption("test"))
        then:
        res != null
        res.value == 'Hello[ru_RU]test'
    }

    def "LocaleConvertOption not exist"() {
        when:
        def converter = new LocaleIOConverter()
        def res = converter.convert(new Input("Hello"))
        then:
        res != null
        res.value == 'Hello[null]'
    }

    def "IncludeConvertOption exist"() {
        when:
        def converter = new IncludeIOConverter()
        def res = converter.convert(new Input("Hello"), new IncludeConvertOption("value, value2"))
        then:
        res != null
        res.value == 'Hello[value][value2]'
    }
}
