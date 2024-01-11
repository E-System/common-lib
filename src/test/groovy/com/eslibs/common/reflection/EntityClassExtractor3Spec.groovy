package com.eslibs.common.reflection

import com.eslibs.common.reflection.fixture.EntityType1
import com.eslibs.common.reflection.fixture.EntityType2
import com.eslibs.common.reflection.fixture.EntityType3
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
}
