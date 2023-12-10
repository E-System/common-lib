package com.eslibs.common.file;

import java.nio.file.Path;

public record FileResource(

    Path path,
    boolean windows,
    boolean external
) {

    public static FileResource of(Path path, boolean windows, boolean external) {
        return new FileResource(path, windows, external);
    }

    public String getFullPath() {
        return (external ? ("file:" + (windows ? "///" : "")) : "jar:") + path;
    }
}