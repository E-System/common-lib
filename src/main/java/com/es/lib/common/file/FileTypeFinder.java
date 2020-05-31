package com.es.lib.common.file;

import java.util.*;

final class FileTypeFinder {

    private static final Map<FileType, Collection<String>> EXTENSIONS = new HashMap<>();
    private static final Map<String, FileType> EXTENSION_TYPES = new HashMap<>();

    static {
        EXTENSIONS.put(FileType.WORD, Arrays.asList("doc", "docx", "rtf", "odt"));
        EXTENSIONS.put(FileType.EXCEL, Arrays.asList("xls", "xlsx"));
        EXTENSIONS.put(FileType.PDF, Collections.singletonList("pdf"));
        EXTENSIONS.put(FileType.TEXT, Collections.singletonList("txt"));
        EXTENSIONS.put(FileType.ARCHIVE, Arrays.asList("zip", "rar", "bz2", "gz2", "7z"));
        EXTENSIONS.put(FileType.IMAGE, Arrays.asList("png", "jpg", "jpeg", "gif", "bmp"));
        EXTENSIONS.put(FileType.CODE, Arrays.asList("xml", "csv", "java", "cpp", "h"));
        EXTENSIONS.put(FileType.VIDEO, Arrays.asList("avi", "mkv", "mov"));
        EXTENSIONS.put(FileType.POWERPOINT, Arrays.asList("ppt", "pptx", "pps", "ppsx"));
        for (Map.Entry<FileType, Collection<String>> entry : EXTENSIONS.entrySet()) {
            for (String ext : entry.getValue()) {
                EXTENSION_TYPES.put(ext, entry.getKey());
            }
        }
    }

    private FileTypeFinder() { }

    static FileType get(String fileName) {
        return EXTENSION_TYPES.getOrDefault(IO.extension(fileName), FileType.OTHER);
    }
}
