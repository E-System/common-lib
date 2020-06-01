package com.es.lib.common.converter;

import com.es.lib.common.collection.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class BaseConverter<R, T> {

    public Collection<R> convert(Collection<T> items) {
        return convert(items, null);
    }

    public Collection<R> convert(Collection<T> items, BiConsumer<T, R> enhancer) {
        if (Items.isEmpty(items)) {
            return new ArrayList<>();
        }
        return items.stream().filter(Objects::nonNull).map(v -> convert(v, enhancer)).collect(Collectors.toList());
    }

    public R convert(T item) {
        return convert(item, null);
    }

    public R convert(T item, BiConsumer<T, R> enhancer) {
        if (item == null) {
            return null;
        }
        R result = realConvert(item);
        if (enhancer != null) {
            enhancer.accept(item, result);
        }
        return result;
    }

    protected abstract R realConvert(T item);
}