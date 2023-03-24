package com.es.lib.common.collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Values {
    private final List<String> items;

    public static Values of(String... items) {
        return new Values(items != null ? Arrays.asList(items) : Collections.singletonList(null));
    }

    public boolean isEmpty() {
        return items.stream().noneMatch(StringUtils::isNotBlank);
    }

    public String get(int index) {
        return items.get(index);
    }
}
