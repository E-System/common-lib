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

package com.es.lib.common.reflection

import spock.lang.Specification

import java.lang.annotation.Annotation
import java.lang.annotation.Documented

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 22.12.14
 */
class ReflectionUtilSpec extends Specification {

    def "ExtractTypes"() {
        expect:
        ReflectionUtil.extractTypes(type) == result
        where:
        type                  | result
        new IntInst().class   | [Integer.class]
        new ShortInst().class | [Short.class]
        new Mixin().class     | [Integer.class, Short.class]
    }

    def "ExtractClass"() {
        expect:
        ReflectionUtil.extractClass(type) == result
        where:
        type                  | result
        new IntInst().class   | IntInst.class
        new ShortInst().class | ShortInst.class
        new Mixin().class     | Mixin.class
    }

    def "GetAnnotation"() {
        expect:
        ReflectionUtil.getAnnotation(type, annotation) == result
        where:
        type                    | annotation     | result
        AnnotatedClass.class    | TestAnnotation | new TestAnnotationQualifier()
        NotAnnotatedClass.class | TestAnnotation | null
        AnnotatedClass.class    | Documented     | null
    }

    def "GetStaticObjects"() {
        expect:
        ReflectionUtil.getStaticObjects(holder) == vals
        where:
        holder            | vals
        Versions.EMPTY    | ["1.0"]
        Versions.MOSQUITO | ["1.0"]
        Versions.WINDOW   | ["1.0", "1.1", "1.2", "2.0"]
        Versions.UNKNOWN  | []
    }

    def "GetInnerClassByName"() {
        expect:
        ReflectionUtil.getInnerClassByName(holder, name) == val
        where:
        holder   | name       | val
        Versions | "EMPTY"    | Versions.EMPTY
        Versions | "MOSQUITO" | Versions.MOSQUITO
        Versions | "WINDOW"   | Versions.WINDOW
        Versions | "UNKNOWN"  | Versions.UNKNOWN
    }

    def "GetInnerClassesGroupedByName"() {
        expect:
        ReflectionUtil.getInnerClassesGroupedByName(holder) == vals
        where:
        holder   | vals
        Versions | ["EMPTY": Versions.EMPTY, "MOSQUITO": Versions.MOSQUITO, "WINDOW": Versions.WINDOW, "UNKNOWN": Versions.UNKNOWN]
    }

    def "GetInnerClassStaticObjectByName"() {
        expect:
        ReflectionUtil.getInnerClassStaticObjectByName(holder, name) == vals
        where:
        holder   | name       | vals
        Versions | "EMPTY"    | ["1.0"]
        Versions | "MOSQUITO" | ["1.0"]
        Versions | "WINDOW"   | ["1.0", "1.1", "1.2", "2.0"]
        Versions | "UNKNOWN"  | []
    }

    private static class TestAnnotationQualifier implements TestAnnotation {

        @Override
        Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }

    @TestAnnotation
    private static class AnnotatedClass {

    }

    private static class NotAnnotatedClass {

    }

    private static class Inst<T> {

    }

    private static class Inst2<T, V> {

    }

    private static class IntInst extends Inst<Integer> {

    }

    private static class ShortInst extends Inst<Short> {

    }

    private static class Mixin extends Inst2<Integer, Short> {

    }


    private static class Versions {

        public static final class EMPTY {

            public static final String v_1_0 = "1.0";
        }

        public static final class MOSQUITO {

            public static final String v_1_0 = "1.0";
        }

        public static final class WINDOW {

            public static final String v_1_0 = "1.0";
            public static final String v_1_1 = "1.1";
            public static final String v_1_2 = "1.2";
            public static final String v_2_0 = "2.0";
        }

        public static final class UNKNOWN {

        }
    }
}
