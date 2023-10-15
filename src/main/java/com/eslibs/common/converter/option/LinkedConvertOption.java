package com.eslibs.common.converter.option;

import com.eslibs.common.converter.ConvertOption;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class LinkedConvertOption<T, R> extends HashMap<T, R> implements ConvertOption {

    public LinkedConvertOption(Map<? extends T, ? extends R> m) {
        super(m);
    }

    public LinkedConvertOption(T id, R data) {
        put(id, data);
    }
}
