package com.es.lib.common.reflection

import spock.lang.Specification

class EntityClassExtractorSpec extends Specification {

    static class SingleExtractor extends EntityClassExtractor<String> {}

    static class IntermediateClass<T> extends EntityClassExtractor<T> {}
    static class SubClass extends IntermediateClass<Integer> {}

    static class PairExtractor extends DoubleEntityClassExtractor<String, Long> {}

    def "Extract single entity class and instantiate"() {
        given:
        SingleExtractor extractor = new SingleExtractor()

        expect:
        extractor.getEntityClass() == String
        extractor.createInstance() == ""
    }

    def "Extract from subclass hierarchy"() {
        given:
        SubClass extractor = new SubClass()

        expect:
        extractor.getEntityClass() == Integer
    }

    def "Extract double entity classes and instantiate"() {
        given:
        PairExtractor extractor = new PairExtractor()

        expect:
        extractor.getEntityClass() == String
        extractor.getSecondEntityClass() == Long
        extractor.createInstance() == ""
    }

    def "Throw exception when entity class not determined"() {
        given:
        EntityClassExtractor raw = new EntityClassExtractor()

        when:
        raw.getEntityClass()

        then:
        thrown(IllegalArgumentException)
    }
}
