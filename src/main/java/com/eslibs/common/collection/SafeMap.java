package com.eslibs.common.collection;

import com.eslibs.common.date.DateParser;
import com.eslibs.common.date.Dates;
import com.eslibs.common.number.NumberParser;
import com.eslibs.common.number.Numbers;

import java.util.HashMap;
import java.util.Map;

public class SafeMap extends HashMap<String, String> {

    SafeMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public NumberParser number(String key) {
        return Numbers.parser(get(key));
    }

    public DateParser date(String key) {
        return Dates.parser(get(key));
    }

}