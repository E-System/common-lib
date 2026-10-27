package com.es.lib.common.sequence

import com.es.lib.common.exception.ESRuntimeException
import spock.lang.Specification

import java.lang.reflect.Type

class NumberSequenceSpec extends Specification {

    static class ShortHolder<T extends Short> {}
    static class IntHolder<T extends Integer> {}
    static class LongHolder<T extends Long> {}
    static class DoubleHolder<T extends Double> {}

    def "IntSequence decrements and clears"() {
        given:
        IntSequence seq = new IntSequence()

        expect:
        seq.next() == -1
        seq.next() == -2

        when:
        seq.clear()

        then:
        seq.next() == -1
    }

    def "LongSequence decrements and clears"() {
        given:
        LongSequence seq = new LongSequence()

        expect:
        seq.next() == -1L
        seq.next() == -2L

        when:
        seq.clear()

        then:
        seq.next() == -1L
    }

    def "ShortSequence decrements and clears"() {
        given:
        ShortSequence seq = new ShortSequence()

        expect:
        seq.next() == (short) -1
        seq.next() == (short) -2

        when:
        seq.clear()

        then:
        seq.next() == (short) -1
    }

    def "NumberSequence factory create by Type"() {
        given:
        Type shortType = new ShortHolder<Short>() {}.getClass().getGenericSuperclass()
        Type intType = new IntHolder<Integer>() {}.getClass().getGenericSuperclass()
        Type longType = new LongHolder<Long>() {}.getClass().getGenericSuperclass()
        Type doubleType = new DoubleHolder<Double>() {}.getClass().getGenericSuperclass()

        expect:
        NumberSequence.create(shortType) instanceof ShortSequence
        NumberSequence.create(intType) instanceof IntSequence
        NumberSequence.create(longType) instanceof LongSequence

        when:
        NumberSequence.create(doubleType)

        then:
        thrown(ESRuntimeException)
    }
}
