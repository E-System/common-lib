package com.eslibs.common

import com.eslibs.common.Jsons
import com.fasterxml.jackson.core.type.TypeReference
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

    def "java 8 date/time"() {
        when:
        def zdt = ZonedDateTime.now()
        def odt = OffsetDateTime.now()
        def item = new DateClass(zdt, odt)
        def res = Jsons.toJson(item)
        def deserialized = Jsons.fromJson(res, DateClass)
        then:
        res == '{"field1":"' + DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zdt) + '","field2":"' + DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(odt) + '"}'
        item == deserialized
    }

    def "Clone"(){
        when:
        def zdt = ZonedDateTime.now()
        def odt = OffsetDateTime.now()
        def item = new DateClass(zdt, odt)
        def deserialized = Jsons.clone(item, DateClass)
        then:
        item == deserialized
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

    static class DateClass {
        ZonedDateTime field1
        OffsetDateTime field2

        DateClass() {}

        DateClass(ZonedDateTime field1, OffsetDateTime field2) {
            this.field1 = field1
            this.field2 = field2
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            DateClass dateClass = (DateClass) o

            if (field1 != dateClass.field1) return false
            if (field2 != dateClass.field2) return false

            return true
        }

        int hashCode() {
            int result
            result = (field1 != null ? field1.hashCode() : 0)
            result = 31 * result + (field2 != null ? field2.hashCode() : 0)
            return result
        }
    }
}
