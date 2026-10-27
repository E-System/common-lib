package com.es.lib.common.reflection

import spock.lang.Specification

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class InstantiatorSpec extends Specification {

    static class GenericHolder<A, B> {}

    def "Create with classes and instantiate"() {
        given:
        Instantiator<String, ArrayList> instantiator = new Instantiator<>(String, ArrayList)

        expect:
        instantiator.getEntityClass() == String
        instantiator.getSecondEntityClass() == ArrayList
        instantiator.createInstance() == ""
        instantiator.createSecondInstance() == []
    }

    def "Create from Type"() {
        given:
        Type type = new GenericHolder<String, HashSet>() {}.getClass().getGenericSuperclass()

        when:
        Instantiator instantiator = Instantiator.create(type, Object)

        then:
        instantiator.getEntityClass() == String
        instantiator.getSecondEntityClass() == HashSet
        instantiator.createInstance() == ""
        instantiator.createSecondInstance() instanceof HashSet
    }
}
