package com.es.lib.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range<T> {

    private T from;
    private T to;

    public static <T> Range<T> empty() {
        return new Range<>();
    }
}