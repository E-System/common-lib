package com.es.lib.common.model;

import lombok.Data;

@Data
public class Range<T> {

    private T from;
    private T to;

    public static <T> Range<T> empty() {
        return new Range<>();
    }
}