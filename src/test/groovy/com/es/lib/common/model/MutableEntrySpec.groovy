package com.es.lib.common.model

import spock.lang.Specification

class MutableEntrySpec extends Specification {

    def "Getters and setters"() {
        given:
        MutableEntry<String, Integer> entry = new MutableEntry<>("k1", 100)

        expect:
        entry.key == "k1"
        entry.value == 100

        when:
        def oldKey = entry.setKey("k2")
        def oldValue = entry.setValue(200)

        then:
        oldKey == "k1"
        oldValue == 100
        entry.key == "k2"
        entry.value == 200
        entry.toString() == "k2=200"
    }

    def "Copy constructor"() {
        given:
        Map.Entry<String, String> original = new AbstractMap.SimpleEntry<>("key", "val")

        when:
        MutableEntry<String, String> copy = new MutableEntry<>(original)

        then:
        copy.key == "key"
        copy.value == "val"
    }

    def "Equals and HashCode"() {
        given:
        MutableEntry<String, String> e1 = new MutableEntry<>("a", "b")
        MutableEntry<String, String> e2 = new MutableEntry<>("a", "b")
        MutableEntry<String, String> e3 = new MutableEntry<>("a", "c")
        MutableEntry<String, String> e4 = new MutableEntry<>(null, null)
        MutableEntry<String, String> e5 = new MutableEntry<>(null, null)

        expect:
        e1.equals(e2)
        e1.hashCode() == e2.hashCode()
        !e1.equals(e3)
        !e1.equals("Not an entry")
        e4.equals(e5)
        e4.hashCode() == e5.hashCode()
        !e4.equals(e1)
    }
}
