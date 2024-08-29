package com.eslibs.common.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum FileType {

    WORD(List.of("doc", "docx", "rtf", "odt")),
    EXCEL(List.of("xls", "xlsx")),
    PDF(List.of("pdf")) {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    TEXT(List.of("txt")),
    ARCHIVE(List.of("zip", "rar", "bz2", "gz2", "7z", "gz")),
    IMAGE(List.of("png", "jpg", "jpeg", "gif", "bmp")) {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    CODE(List.of("xml", "csv", "java", "cpp", "h")),
    VIDEO(List.of("avi", "mkv", "mov")) {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    POWERPOINT(List.of("ppt", "pptx", "pps", "ppsx")),
    OTHER(null) {
        @Override
        public String getIcon() {
            return "file-o";
        }
    };

    private final Collection<String> extensions;

    public static FileType of(String fileName) {
        return EXTENSION_TYPES.getOrDefault(IO.extension(fileName), FileType.OTHER);
    }

    private static final Map<String, FileType> EXTENSION_TYPES = Stream.of(FileType.values())
        .filter(v -> v.getExtensions() != null)
        .flatMap(v -> v.getExtensions().stream().map(k -> Map.entry(k, v)))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public String getIcon() {
        return "file-" + toString().toLowerCase() + "-o";
    }

    public boolean isCanOpen() {
        return false;
    }
}