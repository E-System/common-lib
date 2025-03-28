package com.es.lib.common.converter.option;

import com.es.lib.common.StringSplitter;
import com.es.lib.common.converter.ConvertOption;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;

public class IncludeConvertOption implements ConvertOption {

    private final java.util.Set<String> items;

    public IncludeConvertOption(String items) {
        this(StringUtils.isBlank(items) ? Collections.emptySet() : new LinkedHashSet<>(StringSplitter.process(items, StringSplitter.splitter(","))));
    }

    public IncludeConvertOption(java.util.Set<String> items) {
        this.items = items;
    }

    public <R, T> R process(T in, R out, IncludeConsumer<T, R> consumer) {
        for (String item : items) {
            consumer.accept(item, in, out);
        }
        return out;
    }

    public interface IncludeConsumer<T, R> {

        void accept(String field, T in, R out);
    }

}