package com.eslibs.common.reflection

import com.eslibs.common.reflection.fixture.EntityType1
import com.eslibs.common.reflection.fixture.EntityType2
import com.eslibs.common.reflection.fixture.EntityType3
import spock.lang.Specification

class InstanceCreatorSpec extends Specification {

    def "Extract"() {
        expect:
        with(value) {
            with(createInstance()) {
                it.class == EntityType1.class
                (it as EntityType1).id == 1
            }
            with(createSecondInstance()) {
                it.class == EntityType2.class
                (it as EntityType2).id == 2
            }
            with(createThirdInstance()) {
                it.class == EntityType3.class
                (it as EntityType3).id == 3
            }
        }
        where:
        value << [new TestClass(), new TestClass2(), new TestClass3()]
    }

    def "Extract with proxy"() {
        when:
        def vals = [:]
        def proxy = ProxyFactory.create(TestClass.class, vals)
        then:
        with(proxy.createThirdInstance()) {
            it.class == EntityType3.class
            (it as EntityType3).id == 3
        }
    }

    static class BaseClass<T1, T2, T3> {}

    static class TestClass extends BaseClass<EntityType1, EntityType2, EntityType3> {

        GenericDescriptor creator = GenericDescriptor.create(getClass(), EntityType2.class, EntityType3.class)

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

    static class TestClass2 extends TestClass {}

    static class TestClass3 extends TestClass2 {}
}
