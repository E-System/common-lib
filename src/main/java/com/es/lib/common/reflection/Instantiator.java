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

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class Instantiator<E1, E2, E3> implements Serializable {

    private Class<E1> entityClass;
    private Class<E2> secondEntityClass;
    private Class<E3> thirdEntityClass;

    public Instantiator(Class<E1> entityClass, Class<E2> secondEntityClass, Class<E3> thirdEntityClass) {
        this.entityClass = entityClass;
        this.secondEntityClass = secondEntityClass;
        this.thirdEntityClass = thirdEntityClass;
    }

    public static Instantiator create(Type it, Class<?> defaultSecondClass, Class<?> defaultThirdClass) {
        Type[] entityTypes = ReflectionUtil.extractTypes(it);
        Class<?> entityClass = ReflectionUtil.extractClass(entityTypes[0]);
        Class<?> secondEntityClass = ReflectionUtil.extractClass(entityTypes.length > 1 ? entityTypes[1] : defaultSecondClass);
        Class<?> thirdEntityClass = ReflectionUtil.extractClass(entityTypes.length > 2 ? entityTypes[2] : defaultThirdClass);
        return new Instantiator<>(entityClass, secondEntityClass, thirdEntityClass);
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
        try {
            return getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ESRuntimeException(e);
        }
    }

    public E2 createSecondInstance() {
        try {
            return getSecondEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ESRuntimeException(e);
        }
    }

    public E3 createThirdInstance() {
        try {
            return getThirdEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ESRuntimeException(e);
        }
    }
}
