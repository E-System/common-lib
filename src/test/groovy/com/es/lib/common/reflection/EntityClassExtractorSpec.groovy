package com.es.lib.common.reflection

import spock.lang.Specification

class EntityClassExtractorSpec extends Specification {

    def "Extract class"() {
        when:
        def item = new TestClass()
        then:
        item.getEntityClass() == EntityType1.class
        item.createInstance().id == 1
    }

    class TestClass extends EntityClassExtractor<EntityType1> {

    }

    static class EntityType1 {
        Integer id

        EntityType1() {
            id = 1
        }
    }
}
