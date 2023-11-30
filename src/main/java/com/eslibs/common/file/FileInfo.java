package com.eslibs.common.file;

import java.nio.file.Path;

public record FileInfo(
    Path path,
    FileName fileName,
    long size,
    long crc32,
    String mime
) {

    public FileInfo(FileName fileName, long size, long crc32, String mime) {
        this(null, fileName, size, crc32, mime);
    }

    public FileInfo(String name, String ext, long size, long crc32, String mime) {
        this(null, FileName.of(name, ext), size, crc32, mime);
    }
}