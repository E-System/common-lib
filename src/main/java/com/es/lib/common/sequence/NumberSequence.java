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

package com.es.lib.common.sequence;

import com.es.lib.common.exception.ESRuntimeException;
import com.es.lib.common.reflection.ReflectionUtil;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Deprecated
public abstract class NumberSequence<T extends Number> implements Serializable {

    protected T value;

    public NumberSequence() {
        clear();
    }

    public static NumberSequence create(Type type) {
        Class<?> entityClass = (Class<?>) ReflectionUtil.extractTypes(type)[0];
        if (entityClass.isAssignableFrom(Short.class)) {
            return new ShortSequence();
        } else if (entityClass.isAssignableFrom(Integer.class)) {
            return new IntSequence();
        } else if (entityClass.isAssignableFrom(Long.class)) {
            return new LongSequence();
        }
        throw new ESRuntimeException("Parametrize with invalid types. Available Short, Integer, Long but used: " + entityClass);
    }

    public abstract T next();

    public abstract void clear();
}
