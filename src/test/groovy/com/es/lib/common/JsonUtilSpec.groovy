package com.es.lib.common

import com.fasterxml.jackson.core.type.TypeReference
import spock.lang.Specification

class JsonUtilSpec extends Specification {

    def "Value"() {
        when:
        def map = ['key1': 'value', 'key2': 3]
        def collection = [['key1': 'value', 'key2': 3], ['key1': 'value2', 'key2': 4]]
        def res = JsonUtil.convert(map, ForJsonTest.class)
        def res2 = JsonUtil.convert(collection, new TypeReference<Collection<ForJsonTest>>() {})
        println(res)
        println(res2)
        then:
        noExceptionThrown()
        res != null
        res.key1 == 'value'
        res.key2 == 3
        res2.size() == 2
    }

    def "ToJson and FromJson methods"() {
        given:
        ForJsonTest obj = new ForJsonTest("test", 42)

        when:
        String json = JsonUtil.toJson(obj)

        then:
        json != null
        json.contains('"key1":"test"')

        when:
        ForJsonTest fromStr = JsonUtil.fromJson(json, ForJsonTest)
        ForJsonTest fromTypeRef = JsonUtil.fromJson(json, new TypeReference<ForJsonTest>() {})
        ForJsonTest fromReader = JsonUtil.fromJson(new StringReader(json), ForJsonTest)
        ForJsonTest fromReaderTypeRef = JsonUtil.fromJson(new StringReader(json), new TypeReference<ForJsonTest>() {})

        then:
        fromStr.key1 == "test"
        fromStr.key2 == 42
        fromTypeRef.key1 == "test"
        fromReader.key1 == "test"
        fromReaderTypeRef.key1 == "test"
    }

    static class ForJsonTest {
        String key1
        Integer key2

        ForJsonTest() {
        }

        ForJsonTest(String key1, Integer key2) {
            this.key1 = key1
            this.key2 = key2
        }

        String getKey1() {
            return key1
        }

        void setKey1(String key1) {
            this.key1 = key1
        }

        Integer getKey2() {
            return key2
        }

        void setKey2(Integer key2) {
            this.key2 = key2
        }


        @Override
        public String toString() {
            return "ForJsonTest{" +
                    "key1='" + key1 + '\'' +
                    ", key2=" + key2 +
                    '}';
        }
    }
}
