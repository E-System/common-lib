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
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class CollectionUtil {

    private CollectionUtil() {}

    /**
     * Создать новый ассоциативный массив без пустых значений
     *
     * @param map исходный ассоциативный массив
     * @param <K> тип ключа
     * @return новый ассоциативный массив без пустых значений
     */
    public static <K> Map<K, String> removeEmptyValues(Map<K, String> map) {
        if (CollectionUtil.isEmpty(map)) {
            return map;
        }
        return map.entrySet().stream().filter(
            v -> v.getValue() != null && (!(v.getValue() instanceof CharSequence) || StringUtils.isNotEmpty(v.getValue()))
        ).collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            )
        );
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

    /**
     * Split array on N array with max {count} size
     *
     * @param list  input array
     * @param count max any output array size
     * @param <T>   type of array
     * @return Array of N array
     */
    public static <T> List<List<T>> partition(List<T> list, int count) {
        List<List<T>> parts = new ArrayList<>();
        if (isNotEmpty(list) && count > 0) {
            final int size = list.size();
            for (int i = 0; i < size; i += count) {
                parts.add(new ArrayList<>(
                    list.subList(i, Math.min(size, i + count)))
                );
            }
        }
        return parts;
    }

    /**
     * Split array in {count} array
     *
     * @param list    input array
     * @param count   size splitted array
     * @param shuffle shuffle element into arrays
     * @param <T>     type of array
     * @return {count} size array
     */
    public static <T> List<List<T>> partitionOn(List<T> list, int count, boolean shuffle) {
        List<List<T>> parts = new ArrayList<>();
        if (isNotEmpty(list) && count > 0) {
            for (int i = 0; i < count; ++i) {
                parts.add(new LinkedList<>());
            }
            if (!shuffle) {
                int size = list.size();
                int partSize = (int) Math.round(Math.ceil(1.0d * size / count));
                for (int i = 0; i < count && size > 0; ++i) {
                    parts.get(i).addAll(
                        list.subList(partSize * i, partSize * i + Math.min(partSize, size))
                    );
                    size -= partSize;
                }
            } else {
                int index = 0;
                for (T item : list) {
                    if (index >= count) {
                        index = 0;
                    }
                    List<T> ts = parts.get(index);
                    ts.add(item);
                    ++index;
                }
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

    /**
     * Fill not exist map values by keys
     *
     * @param map   map
     * @param keys  keys array
     * @param value value for added keys
     * @param <K>   key type
     * @param <V>   value type
     * @return input map
     */
    public static <K, V> Map<K, V> fillNotAvailableKeys(Map<K, V> map, Collection<K> keys, V value) {
        if (map == null) {
            return null;
        }
        if (keys == null) {
            return map;
        }
        keys.stream().filter(key -> !map.containsKey(key)).forEach(key -> map.put(key, value));
        return map;
    }

    public static <V> Map<String, V> extractByPrefix(Map<String, V> map, String prefix, boolean removePrefix, int capacity) {
        if (map == null) {
            return null;
        }
        if (StringUtils.isBlank(prefix)) {
            return new HashMap<>();
        }
        Map<String, V> result = new HashMap<>(capacity);
        for (Map.Entry<String, V> entry : map.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String key = entry.getKey();
                if (removePrefix) {
                    key = key.replace(prefix + "_", "");
                }
                result.put(key, entry.getValue());
            }
        }
        return result;
    }

    public static <V> Map<String, V> extractByPrefix(Map<String, V> map, String prefix, boolean removePrefix) {
        return extractByPrefix(map, prefix, removePrefix, 3);
    }

    public static <U> Map.Entry<U, Integer> findWithIndex(final List<U> input, final Predicate<U> predicate) {
        for (int i = 0; i < input.size(); ++i) {
            U elem = input.get(i);
            if (predicate.test(elem)) {
                return Pair.of(elem, i);
            }
        }
        return null;
    }
}
