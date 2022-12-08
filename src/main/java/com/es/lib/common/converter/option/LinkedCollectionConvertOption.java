package com.es.lib.common.converter.option;

import com.es.lib.common.collection.Items;

import java.util.Collection;
import java.util.Map;

public class LinkedCollectionConvertOption<T, R> extends LinkedConvertOption<T, Collection<R>> {

    public LinkedCollectionConvertOption(Map<T, Collection<R>> items) {
        super(items);
    }

    public LinkedCollectionConvertOption(T id, Collection<R> data) {
        super(id, data);
    }

    public R getFirst(T id) {
        return Items.getFirst(get(id));
    }
}
