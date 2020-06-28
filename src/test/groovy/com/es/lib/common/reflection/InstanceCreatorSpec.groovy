package com.es.lib.common.reflection

import spock.lang.Specification

class InstanceCreatorSpec extends Specification {

    def "Extract"() {
        when:
        def cls = new TestClass()
        def o1 = cls.createInstance()
        def o2 = cls.createSecondInstance()
        def o3 = cls.createThirdInstance()
        then:
        o1.class == EntityType1.class
        o2.class == EntityType2.class
        o3.class == EntityType3.class
    }

    class BaseClass<T1, T2, T3> {}

    class TestClass extends BaseClass<EntityType1, EntityType2, EntityType3> {
        InstanceCreator creator = InstanceCreator.create(getClass(), EntityType2.class, EntityType3.class)

        def createInstance() {
            return creator.createInstance()
        }

        def createSecondInstance() {
            return creator.createSecondInstance()
        }

        def createThirdInstance() {
            return creator.createThirdInstance()
        }
    }

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
