package com.es.lib.common.converter;

import com.es.lib.common.collection.Items;
import com.es.lib.common.converter.option.LocaleConvertOption;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 09.02.2021
 */
public abstract class BaseConverter<R, T> {

    public Collection<R> convert(Collection<T> items) {
        return convert(items, null, new ConvertOption[]{});
    }

    public Collection<R> convert(Collection<T> items, ConvertOption... options) {
        return convert(items, null, options);
    }

    public Collection<R> convert(Collection<T> items, BiConsumer<T, R> enhancer, ConvertOption... options) {
        if (Items.isEmpty(items)) {
            return new ArrayList<>();
        }
        return items.stream().filter(Objects::nonNull).map(v -> convert(v, enhancer, options)).collect(Collectors.toList());
    }

    public R convert(T item) {
        return convert(item, null, new ConvertOption[]{});
    }

    public R convert(T item, ConvertOption... options) {
        return convert(item, null, options);
    }

    public R convert(T item, BiConsumer<T, R> enhancer, ConvertOption... options) {
        if (item == null) {
            return null;
        }
        R result = realConvert(item, new HashSet<>(Arrays.asList(options)));
        if (enhancer != null) {
            enhancer.accept(item, result);
        }
        return result;
    }

    protected abstract R realConvert(T item, Set<ConvertOption> options);

    protected <A> Optional<A> getOption(Set<ConvertOption> options, Class<A> cls) {
        return options.stream().filter(v -> v.getClass().isAssignableFrom(cls)).map(v -> (A) v).findFirst();
    }

    protected String getLocaleOption(Set<ConvertOption> options) {
        return getOption(options, LocaleConvertOption.class).orElse(new LocaleConvertOption(null)).getLocale();
    }
}