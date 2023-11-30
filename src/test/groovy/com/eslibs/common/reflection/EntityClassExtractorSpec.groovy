package com.eslibs.common.reflection

import spock.lang.Specification

class EntityClassExtractorSpec extends Specification {

    def "Extract class"() {
        when:
        def item1 = new TestClass1()
        def item2 = new TestClass2()
        def item3 = new TestClass3()
        then:
        item1.getEntityClass() == EntityType1.class
        item1.createInstance().id == 1
        item2.getEntityClass() == EntityType1.class
        item2.getSecondEntityClass() == EntityType2.class
        item2.createInstance().id == 1
        item2.createSecondInstance().id == 2
        item3.getEntityClass() == EntityType1.class
        item3.getSecondEntityClass() == EntityType2.class
        item3.getThirdEntityClass() == EntityType3.class
        item3.createInstance().id == 1
        item3.createSecondInstance().id == 2
        item3.createThirdInstance().id == 3
    }

    class TestClass1 extends EntityClassExtractor<EntityType1> {}

    class TestClass2 extends EntityClassExtractor2<EntityType1, EntityType2> {}

    class TestClass3 extends EntityClassExtractor3<EntityType1, EntityType2, EntityType3> {}

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
