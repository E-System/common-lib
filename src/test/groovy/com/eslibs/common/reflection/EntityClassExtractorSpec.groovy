package com.eslibs.common.reflection

import com.eslibs.common.reflection.fixture.EntityType1
import com.eslibs.common.reflection.fixture.EntityType2
import com.eslibs.common.reflection.fixture.EntityType3
import spock.lang.Specification

class EntityClassExtractorSpec extends Specification {

    def "Extract class"() {
        expect:
        with(value1) {
            getEntityClass() == EntityType1.class
            createInstance().id == 1
        }
        with(value2) {
            getEntityClass() == EntityType1.class
            getSecondEntityClass() == EntityType2.class
            createInstance().id == 1
            createSecondInstance().id == 2
        }
        with(value3) {
            getEntityClass() == EntityType1.class
            getSecondEntityClass() == EntityType2.class
            getThirdEntityClass() == EntityType3.class
            createInstance().id == 1
            createSecondInstance().id == 2
            createThirdInstance().id == 3
        }
        where:
        value1 << [new TestClass1(), new TestClass12(), new TestClass13()]
        value2 << [new TestClass2(), new TestClass22(), new TestClass23()]
        value3 << [new TestClass3(), new TestClass32(), new TestClass33()]
    }

    static class TestClass1 extends EntityClassExtractor<EntityType1> {}

    static class TestClass2 extends EntityClassExtractor2<EntityType1, EntityType2> {}

    static class TestClass3 extends EntityClassExtractor3<EntityType1, EntityType2, EntityType3> {}

    static class TestClass12 extends TestClass1 {}

    static class TestClass22 extends TestClass2 {}

    static class TestClass32 extends TestClass3 {}

    static class TestClass13 extends TestClass12 {}

    static class TestClass23 extends TestClass22 {}

    static class TestClass33 extends TestClass32 {}

}
