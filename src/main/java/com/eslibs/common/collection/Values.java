package com.eslibs.common.collection;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public record Values(List<String> items) {

    public static Values of(String... items) {
        return new Values(items != null ? Arrays.asList(items) : Collections.singletonList(null));
    }

    public boolean isEmpty() {
        return items.stream().noneMatch(StringUtils::isNotBlank);
    }

    public String get(int index) {
        return items.get(index);
    }

    public <T> T get(int index, Function<String, T> converter) {
        return get(index, converter, null);
    }

    public <T> T get(int index, Function<String, T> converter, T defaultValue) {
        String result = get(index);
        return result != null ? converter.apply(result) : defaultValue;
    }
}
