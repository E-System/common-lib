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

import com.es.lib.common.exception.ESRuntimeException;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
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

    public Class<E1> getEntityClass() {
        return entityClass;
    }

    public Class<E2> getSecondEntityClass() {
        return secondEntityClass;
    }

    public Class<E3> getThirdEntityClass() {
        return thirdEntityClass;
    }

    public E1 createInstance() {
        return newInstance(getEntityClass());
    }

    public E2 createSecondInstance() {
        return newInstance(getSecondEntityClass());
    }

    public E3 createThirdInstance() {
        return newInstance(getThirdEntityClass());
    }

    private <T> T newInstance(Class<T> instanceClass) {
        try {
            return instanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ESRuntimeException(e);
        }
    }
}
