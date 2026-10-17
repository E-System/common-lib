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
