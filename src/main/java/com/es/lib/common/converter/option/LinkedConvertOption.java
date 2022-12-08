package com.es.lib.common.converter.option;

import com.es.lib.common.converter.ConvertOption;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class LinkedConvertOption<T, R> implements ConvertOption {
    private final Map<T, R> items;

    public LinkedConvertOption(T id, R data) {
        this(new HashMap<>());
        items.put(id, data);
    }

    public R get(T id) {
        return items.get(id);
    }
}
