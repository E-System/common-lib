package com.es.lib.common.reflection

import spock.lang.Specification

class EntityClassExtractor2Spec extends Specification {

    def "Extract class"() {
        when:
        def item = new TestClass()
        then:
        item.getEntityClass() == EntityType1.class
        item.createInstance().id == 1
        item.getSecondEntityClass() == EntityType2.class
        item.createSecondInstance().id == 2
    }

    class TestClass extends EntityClassExtractor2<EntityType1, EntityType2> {}

    static class EntityType1 {
        Integer id

        EntityType1() {
            id = 1
        }
    }

    static class EntityType2 {
        Integer id

        EntityType2() {
            id = 2
        }
    }
}
