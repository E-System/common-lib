package com.es.lib.common.converter.option;

import com.es.lib.common.collection.Items;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@NoArgsConstructor
public class LinkedCollectionConvertOption<T, R> extends LinkedConvertOption<T, Collection<R>> {

    public LinkedCollectionConvertOption(Map<T, Collection<R>> items) {
        super(items);
    }

    public LinkedCollectionConvertOption(T id, Collection<R> data) {
        super(id, data);
    }

    public LinkedCollectionConvertOption(T id, R data) {
        super(id, Collections.singletonList(data));
    }

    public R getFirst(T id) {
        return Items.getFirst(get(id));
    }
}
