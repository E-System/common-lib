package com.es.lib.common.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@ToString
@RequiredArgsConstructor
public class FileInfo {

    private final Path path;
    private final FileName fileName;
    private final long size;
    private final long crc32;
    private final String mime;

    public FileInfo(FileName fileName, long size, long crc32, String mime) {
        this(null, fileName, size, crc32, mime);
    }
}