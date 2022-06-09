package com.es.lib.common.converter;

import com.es.lib.common.collection.Items;
import com.es.lib.common.converter.option.IncludeConvertOption;
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

    public Collection<R> convert(Collection<T> items, ConvertOption.Set options) {
        return convert(items, null, options);
    }

    public Collection<R> convert(Collection<T> items, BiConsumer<T, R> enhancer, ConvertOption... options) {
        return convert(items, enhancer, new ConvertOption.Set(options));
    }

    public Collection<R> convert(Collection<T> items, BiConsumer<T, R> enhancer, ConvertOption.Set options) {
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

    public R convert(T item, ConvertOption.Set options) {
        return convert(item, null, options);
    }

    public R convert(T item, BiConsumer<T, R> enhancer, ConvertOption... options) {
        return convert(item, enhancer, new ConvertOption.Set(options));
    }

    public R convert(T item, BiConsumer<T, R> enhancer, ConvertOption.Set options) {
        if (item == null) {
            return null;
        }
        R result = realConvert(item, options);
        if (enhancer != null) {
            enhancer.accept(item, result);
        }
        return result;
    }

    protected abstract R realConvert(T item, Set<ConvertOption> options);

    protected <A> Optional<A> getOption(Set<ConvertOption> options, Class<A> cls) {
        return ConvertOption.get(options, cls);
    }

    protected String getLocaleOption(Set<ConvertOption> options) {
        return getOption(options, LocaleConvertOption.class).orElse(new LocaleConvertOption(null)).getLocale();
    }

    protected IncludeConvertOption getIncludeOption(Set<ConvertOption> options) {
        return getOption(options, IncludeConvertOption.class).orElse(new IncludeConvertOption(Collections.emptySet()));
    }
}