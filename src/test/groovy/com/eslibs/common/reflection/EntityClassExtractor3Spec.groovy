package com.eslibs.common.reflection

import spock.lang.Specification

class EntityClassExtractor3Spec extends Specification {

    def "Extract class"() {
        when:
        def item = new TestClass()
        then:
        item.getEntityClass() == EntityType1.class
        item.createInstance().id == 1
        item.getSecondEntityClass() == EntityType2.class
        item.createSecondInstance().id == 2
        item.getThirdEntityClass() == EntityType3.class
        item.createThirdInstance().id == 3
    }

    class TestClass extends EntityClassExtractor3<EntityType1, EntityType2, EntityType3> {}


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

    static class EntityType3 {
        Integer id

        EntityType3() {
            id = 3
        }
    }
}
