package com.es.lib.common

import com.fasterxml.jackson.core.type.TypeReference
import spock.lang.Specification

class JsonsSpec extends Specification {

    def "Class from json"() {
        when:
        def res = Jsons.fromJson('{"field1":"value1", "field2":"value2"}', TestClass)
        then:
        res.field1 == 'value1'
        res.field2 == 'value2'
    }

    def "Map from json"() {
        when:
        def res = Jsons.fromJson('{"key":{"field1":"value1", "field2":"value2"}}', new TypeReference<Map<String, TestClass>>() {
        })
        then:
        res.containsKey('key')
        res['key'].field1 == 'value1'
        res['key'].field2 == 'value2'
    }

    def "Class to json"() {
        when:
        def res = Jsons.toJson(new TestClass('value1', 'value2'))
        then:
        res == '{"field1":"value1","field2":"value2"}'
    }

    def "Map to json"() {
        when:
        def res = Jsons.toJson(['key': new TestClass('value1', 'value2')])
        then:
        res == '{"key":{"field1":"value1","field2":"value2"}}'
    }

    static class TestClass {
        String field1
        String field2

        TestClass() {}

        TestClass(String field1, String field2) {
            this.field1 = field1
            this.field2 = field2
        }
    }
}
