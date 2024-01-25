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

package com.eslibs.common.reflection;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.04.15
 */

public record GenericDescriptor<E1, E2, E3>(
    Class<E1> entityClass,
    Class<E2> secondEntityClass,
    Class<E3> thirdEntityClass
) implements Serializable {

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3> GenericDescriptor<T1, T2, T3> create(Type it, Class<?> defaultSecondClass, Class<?> defaultThirdClass) {
        return (GenericDescriptor<T1, T2, T3>) DESCRIPTOR_HASH.computeIfAbsent(it, v -> {
            Type[] args = Reflects.genericArguments(v);
            Class<T1> entityClass = (Class<T1>) Reflects.extractClass(args[0]);
            Class<T2> secondEntityClass = (Class<T2>) Reflects.extractClass(args.length > 1 ? args[1] : defaultSecondClass);
            Class<T3> thirdEntityClass = (Class<T3>) Reflects.extractClass(args.length > 2 ? args[2] : defaultThirdClass);
            return new GenericDescriptor<>(entityClass, secondEntityClass, thirdEntityClass);
        });
    }

    public E1 createInstance() {
        return Reflects.newInstance(entityClass());
    }

    public E2 createSecondInstance() {
        return Reflects.newInstance(secondEntityClass());
    }

    public E3 createThirdInstance() {
        return Reflects.newInstance(thirdEntityClass());
    }

    private static final Map<Type, GenericDescriptor<?, ?, ?>> DESCRIPTOR_HASH = new ConcurrentHashMap<>();
}
