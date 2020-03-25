package com.es.lib.common.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileResource {

    private final boolean windows;
    private final boolean external;
    private final String path;

    public static FileResource create(boolean windows, boolean external, String path) {
        return new FileResource(windows, external, path);
    }

    public String getFullPath() {
        return (isExternal() ? ("file:" + (windows ? "///" : "")) : "jar:") + getPath();
    }
}