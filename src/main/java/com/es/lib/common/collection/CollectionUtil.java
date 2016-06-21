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

package com.es.lib.common.collection;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * Создать новый ассоциативный массив без null значений
     *
     * @param map исходный ассоциативный массив
     * @param <K> тип ключа
     * @param <V> тип значения
     * @return новый ассоциативный массив без null значений
     */
    public static <K, V> Map<K, V> removeNullValues(Map<K, V> map) {
        if (CollectionUtil.isEmpty(map)) {
            return map;
        }
        return map.entrySet().stream().filter(
                CollectionUtil::isValueNonNull
        ).collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                )
        );
    }

    /**
     * Тест пары на значение == null
     *
     * @param entry пара
     * @return true если значение == null, false если значение != null
     */
    public static boolean isValueNull(Map.Entry<?, ?> entry) {
        return entry == null || entry.getValue() == null;
    }

    /**
     * Тест пары на значение != null
     *
     * @param entry пара
     * @return true если значение != null, false если значение == null
     */
    public static boolean isValueNonNull(Map.Entry<?, ?> entry) {
        return entry != null && entry.getValue() != null;
    }

    /**
     * Тест пары на ключ == null
     *
     * @param entry пара
     * @return true если ключ == null, false если ключ != null
     */
    public static boolean isKeyNull(Map.Entry<?, ?> entry) {
        return entry == null || entry.getKey() == null;
    }

    /**
     * Тест пары на ключ != null
     *
     * @param entry пара
     * @return true если ключ != null, false если ключ == null
     */
    public static boolean isKeyNonNull(Map.Entry<?, ?> entry) {
        return entry != null && entry.getKey() != null;
    }

    public static <T> void iterate(Collection<T> values, BiConsumer<Integer, T> iterator) {
        if (isNotEmpty(values)) {
            int index = 0;
            for (T value : values) {
                iterator.accept(index++, value);
            }
        }
    }

    public static boolean isSizeEqual(Collection<?> collection, int size) {
        if (collection == null) {
            return size == 0;
        }
        return collection.size() == size;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> List<List<T>> partition(List<T> list, int count) {
        List<List<T>> parts = new ArrayList<>();
        if (isNotEmpty(list)) {
            final int size = list.size();
            for (int i = 0; i < size; i += count) {
                parts.add(new ArrayList<>(
                        list.subList(i, Math.min(size, i + count)))
                );
            }
        }
        return parts;
    }

    public static <T> T getFirstOrNull(Collection<T> list) {
        return isEmpty(list) ? null : list.iterator().next();
    }

    public static String joinNotBlank(Collection<String> values, CharSequence delimiter) {
        if (isEmpty(values)) {
            return "";
        }
        return values.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
    }
}
