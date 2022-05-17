package com.es.lib.common.converter;

import com.es.lib.common.collection.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 09.02.2021
 */
public abstract class BaseBiConverter<R, T> extends BaseConverter<R, T> {

    public Collection<T> reverseConvert(Collection<R> items) {
        return reverseConvert(items, null, new ConvertOption[]{});
    }

    public Collection<T> reverseConvert(Collection<R> items, ConvertOption... options) {
        return reverseConvert(items, null, options);
    }

    public Collection<T> reverseConvert(Collection<R> items, ConvertOption.Set options) {
        return reverseConvert(items, null, options);
    }

    public Collection<T> reverseConvert(Collection<R> items, BiConsumer<R, T> enhancer, ConvertOption... options) {
        return reverseConvert(items, enhancer, new ConvertOption.Set(options));
    }

    public Collection<T> reverseConvert(Collection<R> items, BiConsumer<R, T> enhancer, ConvertOption.Set options) {
        if (Items.isEmpty(items)) {
            return new ArrayList<>();
        }
        return items.stream().filter(Objects::nonNull).map(v -> reverseConvert(v, enhancer, options)).collect(Collectors.toList());
    }

    public T reverseConvert(R item) {
        return reverseConvert(item, null, new ConvertOption[]{});
    }

    public T reverseConvert(R item, ConvertOption... options) {
        return reverseConvert(item, null, options);
    }

    public T reverseConvert(R item, ConvertOption.Set options) {
        return reverseConvert(item, null, options);
    }

    public T reverseConvert(R item, BiConsumer<R, T> enhancer, ConvertOption... options) {
        return reverseConvert(item, enhancer, new ConvertOption.Set(options));
    }

    public T reverseConvert(R item, BiConsumer<R, T> enhancer, ConvertOption.Set options) {
        if (item == null) {
            return null;
        }
        T result = realReverseConvert(item, options);
        if (enhancer != null) {
            enhancer.accept(item, result);
        }
        return result;
    }

    protected abstract T realReverseConvert(R item, Set<ConvertOption> options);
}