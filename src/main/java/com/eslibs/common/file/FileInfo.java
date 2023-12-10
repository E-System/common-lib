package com.eslibs.common.file;

import java.nio.file.Path;

public record FileInfo(
    Path path,
    FileName fileName,
    long size,
    long crc32,
    String mime
) {

    public static FileInfo of(FileName fileName, long size, long crc32, String mime) {
        return new FileInfo(null, fileName, size, crc32, mime);
    }

    public static FileInfo of(String name, String ext, long size, long crc32, String mime) {
        return of(FileName.of(name, ext), size, crc32, mime);
    }
}