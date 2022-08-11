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
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class Items {

    private Items() {}

    /**
     * Create new map without empty values
     *
     * @param map source map
     * @param <K> map key type
     * @return map without empty values
     */
    public static <K> Map<K, String> removeEmptyValues(Map<K, String> map) {
        if (isEmpty(map)) {
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
     * Create new map without null values
     *
     * @param map source map
     * @param <K> map key type
     * @param <V> map value type
     * @return map without null values
     */
    public static <K, V> Map<K, V> removeNullValues(Map<K, V> map) {
        if (isEmpty(map)) {
            return map;
        }
        return map.entrySet().stream().filter(
            Items::isValueNonNull
        ).collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            )
        );
    }

    /**
     * Check pair value is null
     *
     * @param entry source pair
     * @return true if pair value is null
     */
    public static boolean isValueNull(Map.Entry<?, ?> entry) {
        return entry == null || entry.getValue() == null;
    }

    /**
     * Check pair value is not null
     *
     * @param entry source pair
     * @return true if pair value is not null
     */
    public static boolean isValueNonNull(Map.Entry<?, ?> entry) {
        return entry != null && entry.getValue() != null;
    }

    /**
     * Check pair key is null
     *
     * @param entry source pair
     * @return true if pair key is null
     */
    public static boolean isKeyNull(Map.Entry<?, ?> entry) {
        return entry == null || entry.getKey() == null;
    }

    /**
     * Check pair key is not null
     *
     * @param entry source pair
     * @return true if pair key is not null
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

    public static <K, V> Map<K, V> map(Map<K, V> map) {
        return map(map, false);
    }

    public static <K, V> Map<K, V> map(Map<K, V> map, boolean immutable) {
        return map == null ? new HashMap<>() : (immutable ? new HashMap<>(map) : map);
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> collection) {
        return immutableMap(collection, false);
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> collection, boolean linked) {
        return collection == null ? null : (linked ? new LinkedHashMap<>(collection) : new HashMap<>(collection));
    }

    public static <T> Set<T> immutableSet(Set<T> collection) {
        return immutableSet(collection, false);
    }

    public static <T> Set<T> immutableSet(Set<T> collection, boolean linked) {
        return collection == null ? null : (linked ? new LinkedHashSet<>(collection) : new HashSet<>(collection));
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

    public static <T> T getFirst(Collection<T> list) {
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

    public static <K, V> Map<K, V> remove(Map<K, V> items, Collection<K> keys) {
        if (isEmpty(items) || isEmpty(keys)) {
            return items;
        }
        keys.forEach(items::remove);
        return items;
    }

    public static <V> Map<String, V> removeByPrefix(Map<String, V> map, String prefix) {
        if (map == null || StringUtils.isBlank(prefix)) {
            return map;
        }
        map.entrySet().removeIf(entry -> entry.getKey().startsWith(prefix));
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

    public static Map<String, String> updateValues(Supplier<Map<String, String>> supplier, Consumer<Map<String, String>> consumer, Collection<? extends Map.Entry<String, String>> items) {
        Map<String, String> attributes = supplier.get();
        if (attributes == null) {
            attributes = new HashMap<>();
            consumer.accept(attributes);
        }
        for (Map.Entry<String, String> item : items) {
            if (StringUtils.isBlank(item.getValue())) {
                attributes.remove(item.getKey());
            } else {
                attributes.put(item.getKey(), item.getValue());
            }
        }
        return attributes;
    }

    public static Map<String, String> updateValues(Supplier<Map<String, String>> supplier, Consumer<Map<String, String>> consumer, Map<String, String> source, Collection<String> keys) {
        Map<String, String> attributes = supplier.get();
        if (attributes == null) {
            attributes = new HashMap<>();
            consumer.accept(attributes);
        }
        for (String key : keys) {
            String value = source.get(key);
            if (StringUtils.isBlank(value)) {
                attributes.remove(key);
            } else {
                attributes.put(key, value);
            }
        }
        return attributes;
    }

    public static <U> Map.Entry<U, Integer> findWithIndex(final List<U> input, final Predicate<U> predicate) {
        if (isEmpty(input)) {
            return null;
        }
        for (int i = 0; i < input.size(); ++i) {
            U elem = input.get(i);
            if (predicate.test(elem)) {
                return Pair.of(elem, i);
            }
        }
        return null;
    }

    public static <T, K> Map<K, List<T>> groupBy(Collection<T> items, Function<? super T, ? extends K> classifier) {
        if (items == null) {
            return new HashMap<>();
        }
        return items.stream().collect(Collectors.groupingBy(classifier));
    }

    @SafeVarargs
    public static <T> T coalesce(T... items) {
        return firstBySelector(Objects::nonNull, items);
    }

    @SafeVarargs
    public static <T> T firstBySelector(Predicate<T> selector, T... items) {
        if (items == null) {
            return null;
        }
        for (T item : items) {
            if (selector.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static <K, V> Map<K, V> toMap(Collection<Map.Entry<K, V>> items) {
        return toMap(items, Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <K, V> Map<K, V> toMap(Collection<V> items, Function<? super V, ? extends K> keyMapper) {
        return toMap(items, keyMapper, v -> v);
    }

    public static <K, V> Map<K, V> toMap(Stream<V> stream, Function<? super V, ? extends K> keyMapper) {
        return stream.collect(Collectors.toMap(keyMapper, t -> t));
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> items, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        if (items == null) {
            return new HashMap<>();
        }
        return toMap(items.stream(), keyMapper, valueMapper);
    }

    public static <T, K, V> Map<K, V> toMap(Stream<T> stream, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T> T cycle(Supplier<List<T>> itemsSource, Supplier<Integer> indexSource, Consumer<Integer> indexDestination) {
        List<T> items = itemsSource.get();
        if (Items.isEmpty(items)) {
            return null;
        }
        if (items.size() == 1) {
            return items.get(0);
        }
        Integer index = indexSource.get();
        if (index == null || index >= items.size()) {
            index = 0;
        }
        T result = items.get(index);
        if (index + 1 < items.size()) {
            ++index;
        } else {
            index = 0;
        }
        indexDestination.accept(index);
        return result;
    }
}
