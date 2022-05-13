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

package com.es.lib.common.reflection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@RequiredArgsConstructor
public class InstanceCreator<E1, E2, E3> implements Serializable {

    private final Class<E1> entityClass;
    private final Class<E2> secondEntityClass;
    private final Class<E3> thirdEntityClass;

    public static <T1, T2, T3> InstanceCreator<T1, T2, T3> create(Type it, Class<?> defaultSecondClass, Class<?> defaultThirdClass) {
        Type[] entityTypes = Reflects.extractTypes(it);
        Class<T1> entityClass = (Class<T1>) Reflects.extractClass(entityTypes[0]);
        Class<T2> secondEntityClass = (Class<T2>) Reflects.extractClass(entityTypes.length > 1 ? entityTypes[1] : defaultSecondClass);
        Class<T3> thirdEntityClass = (Class<T3>) Reflects.extractClass(entityTypes.length > 2 ? entityTypes[2] : defaultThirdClass);
        return new InstanceCreator<>(entityClass, secondEntityClass, thirdEntityClass);
    }

    public E1 createInstance() {
        return Reflects.newInstance(getEntityClass());
    }

    public E2 createSecondInstance() {
        return Reflects.newInstance(getSecondEntityClass());
    }

    public E3 createThirdInstance() {
        return Reflects.newInstance(getThirdEntityClass());
    }
}
