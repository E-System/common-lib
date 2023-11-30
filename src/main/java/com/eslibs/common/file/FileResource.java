package com.eslibs.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileResource {

    private final Path path;
    private final boolean windows;
    private final boolean external;

    public static FileResource of(Path path, boolean windows, boolean external) {
        return new FileResource(path, windows, external);
    }

    public String getFullPath() {
        return (external ? ("file:" + (windows ? "///" : "")) : "jar:") + getPath();
    }
}