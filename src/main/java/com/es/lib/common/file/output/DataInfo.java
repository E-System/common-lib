package com.es.lib.common.file.output;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class DataInfo {

    private final String fileName;
    private final String contentType;
}
