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

package com.es.lib.common;

import com.es.lib.common.exception.ESRuntimeException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class EntityClassExtractor<T> implements Serializable {

	private Class<T> entityClass;

	public Class<T> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				entityClass = extractParametrized(type, 0);
			} else {
				type = getClass().getSuperclass().getGenericSuperclass();
				if (type instanceof ParameterizedType) {
					entityClass = extractParametrized(type, 0);
				} else {
					throw new IllegalArgumentException("Невозможно определить класс сущности");
				}
			}
		}
		return entityClass;
	}

	protected <K> Class<K> extractParametrized(Type type, int index) {
		ParameterizedType paramType = (ParameterizedType) type;
		if (paramType.getActualTypeArguments().length == 2) {
			if (paramType.getActualTypeArguments()[index] instanceof TypeVariable) {
				throw new IllegalArgumentException("Невозможно определить класс сущности");
			} else {
				return (Class<K>) paramType.getActualTypeArguments()[index];
			}
		} else {
			return (Class<K>) paramType.getActualTypeArguments()[index];
		}
	}

	public T createInstance() {
		try {
			return getEntityClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ESRuntimeException(e);
		}
	}
}
