/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.eslibs.common.reflection

import com.eslibs.common.reflection.fixture.TestAnnotation
import spock.lang.Specification

import java.lang.annotation.Annotation
import java.lang.annotation.Documented
import java.util.function.Function

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 22.12.14
 */
class ReflectsSpec extends Specification {

    def "getDeclaredFields"() {
        when:
        def res1 = Reflects.getDeclaredFields(ParentEntityClass)
        def res2 = Reflects.getDeclaredFields(EntityClass)
        def res3 = Reflects.getDeclaredFields(EntityClass2)
        then:
        res1.containsKey('id')
        res2.containsKey('id')
        res2.containsKey('name')
        res3.containsKey('id')
        res3.containsKey('name')
        res3.containsKey('ref')
    }

    def "getMethods"() {
        when:
        def res = Reflects.getDeclaredMethods(ParentEntityClass)
        then:
        res.containsKey('getId')
    }

    def "toMap"() {
        when:
        def res = Reflects.toMap(new EntityClass2(1, "1", new EntityClass(2, "2")), new Function<Object, Object>() {

            @Override
            Object apply(Object o) {
                if (o instanceof ParentEntityClass) {
                    return ((ParentEntityClass) o).getId()
                } else {
                    return o
                }
            }
        })
        then:
        res['id'] == 1
        res['name'] == '1'
        res['ref'] == 2
    }

    def "toMap with exclude"() {
        when:
        def res = Reflects.toMap(new EntityClass2(1, "1", new EntityClass(2, "2")), ['id', 'name'], new Function<Object, Object>() {

            @Override
            Object apply(Object o) {
                if (o instanceof ParentEntityClass) {
                    return ((ParentEntityClass) o).getId()
                } else {
                    return o
                }
            }
        })
        then:
        res['id'] == null
        res['name'] == null
        res['ref'] == 2
    }

    def "ExtractTypes"() {
        expect:
        Reflects.genericArguments(type) == result
        where:
        type                  | result
        new IntInst().class   | [Integer.class]
        new ShortInst().class | [Short.class]
        new Mixin().class     | [Integer.class, Short.class]
    }

    def "ExtractClass"() {
        expect:
        Reflects.extractClass(type) == result
        where:
        type                  | result
        new IntInst().class   | IntInst.class
        new ShortInst().class | ShortInst.class
        new Mixin().class     | Mixin.class
    }

    def "GetAnnotation"() {
        expect:
        Reflects.getAnnotation(type, annotation) == result
        where:
        type                    | annotation     | result
        AnnotatedClass.class    | TestAnnotation | new TestAnnotationQualifier()
        NotAnnotatedClass.class | TestAnnotation | null
        AnnotatedClass.class    | Documented     | null
    }

    def "GetStaticObjects"() {
        expect:
        Reflects.getStaticObjects(holder) == vals
        where:
        holder            | vals
        Versions.EMPTY    | ["1.0"]
        Versions.MOSQUITO | ["1.0"]
        Versions.WINDOW   | ["1.0", "1.1", "1.2", "2.0"]
        Versions.UNKNOWN  | []
    }

    def "GetInnerClassByName"() {
        expect:
        Reflects.getInnerClassByName(holder, name) == val
        where:
        holder   | name       | val
        Versions | "EMPTY"    | Versions.EMPTY
        Versions | "MOSQUITO" | Versions.MOSQUITO
        Versions | "WINDOW"   | Versions.WINDOW
        Versions | "UNKNOWN"  | Versions.UNKNOWN
    }

    def "GetInnerClassesGroupedByName"() {
        expect:
        Reflects.getInnerClassesGroupedByName(holder) == vals
        where:
        holder   | vals
        Versions | ["EMPTY": Versions.EMPTY, "MOSQUITO": Versions.MOSQUITO, "WINDOW": Versions.WINDOW, "UNKNOWN": Versions.UNKNOWN]
    }

    def "GetInnerClassStaticObjectByName"() {
        expect:
        Reflects.getInnerClassStaticObjectByName(holder, name) == vals
        where:
        holder   | name       | vals
        Versions | "EMPTY"    | ["1.0"]
        Versions | "MOSQUITO" | ["1.0"]
        Versions | "WINDOW"   | ["1.0", "1.1", "1.2", "2.0"]
        Versions | "UNKNOWN"  | []
    }

    def "createDefaultInstance Integer"() {
        when:
        def result = Reflects.createDefaultInstance(Integer)
        then:
        result != null
        result == 0
        result instanceof Integer
    }

    def "createDefaultInstance Short"() {
        when:
        def result = Reflects.createDefaultInstance(Short)
        then:
        result != null
        result == 0
        result instanceof Short
    }

    def "createDefaultInstance Long"() {
        when:
        def result = Reflects.createDefaultInstance(Long)
        then:
        result != null
        result == 0
        result instanceof Long
    }

    def "createDefaultInstance Double"() {
        when:
        def result = Reflects.createDefaultInstance(Double)
        then:
        result != null
        result == 0
        result instanceof Double
    }

    def "createDefaultInstance String"() {
        when:
        def result = Reflects.createDefaultInstance(String)
        then:
        result != null
        result == ''
        result instanceof String
    }

    def "createDefaultInstance SomeClass without default constructor return input class"() {
        when:
        def result = Reflects.createDefaultInstance(SomeClass)
        then:
        result != null
        result instanceof Class<?>
    }

    def "createDefaultInstance SomeClass with default constructor"() {
        when:
        def result = Reflects.createDefaultInstance(SomeClass2)
        then:
        result != null
        result instanceof SomeClass2
        ((SomeClass2) result).i == 0
    }

    def "getTypesAnnotatedWith"() {
        when:
        def res = Reflects.getTypesAnnotatedWith(["com.eslibs"], TestAnnotation.class)
        def res2 = Reflects.getTypesAnnotatedWith("com.eslibs", TestAnnotation.class)
        then:
        res.size() == 1
        res2.size() == 1
        res == res2
        res[0] instanceof Class && res2[0] instanceof Class
    }

    def "getResources"() {
        when:
        def res = Reflects.getResources("com.eslibs", null)
        def res2 = Reflects.getResources(["com.eslibs"], null)
        println(res)
        then:
        res.size() > 0
        res.contains('com/eslibs/common/build.properties')
        res.contains('com/eslibs/common/currlist.xml')
        res2.size() > 0
        res2.contains('com/eslibs/common/build.properties')
        res2.contains('com/eslibs/common/currlist.xml')
    }

    private static class SomeClass {

        Integer i

        SomeClass(Integer i, boolean ok) {

        }
    }

    private static class SomeClass2 {

        Integer i

        SomeClass2(Integer i) {
            this.i = i
        }
    }

    private static class TestAnnotationQualifier implements TestAnnotation {

        @Override
        Class<? extends Annotation> annotationType() {
            return TestAnnotation.class
        }
    }

    @TestAnnotation
    private static class AnnotatedClass {

    }

    private static class NotAnnotatedClass {

    }

    private static class Inst<T> {}

    private static class Inst2<T, V> {}

    private static class IntInst extends Inst<Integer> {}

    private static class ShortInst extends Inst<Short> {}

    private static class Mixin extends Inst2<Integer, Short> {}


    private static class Versions {

        static final class EMPTY {

            public static final String v_1_0 = "1.0"
        }

        static final class MOSQUITO {

            public static final String v_1_0 = "1.0"
        }

        static final class WINDOW {

            public static final String v_1_0 = "1.0"
            public static final String v_1_1 = "1.1"
            public static final String v_1_2 = "1.2"
            public static final String v_2_0 = "2.0"
        }

        static final class UNKNOWN {

        }
    }

    private static class ParentEntityClass {

        private Integer id

        ParentEntityClass(Integer id) {
            this.id = id
        }

        Integer getId() {
            return id
        }
    }

    private static class EntityClass extends ParentEntityClass {

        private String name

        EntityClass(Integer id, String name) {
            super(id)
            this.name = name
        }
    }

    private static class EntityClass2 extends ParentEntityClass {

        private String name
        private EntityClass ref

        EntityClass2(Integer id, String name, EntityClass ref) {
            super(id)
            this.name = name
            this.ref = ref
        }
    }
}
