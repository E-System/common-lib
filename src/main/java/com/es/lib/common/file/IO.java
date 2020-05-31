/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class IO {

    private IO() { }

    public static FileType fileType(String fileName) {
        return FileTypeFinder.get(fileName);
    }

    public static String mime(String fileName) {
        return Mime.get(fileName);
    }

    public static String mime(Path file) {
        return mime(file.toString());
    }

    public static String fileNameDisposition(boolean attachment, String fileName) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(fileName, Charset.defaultCharset().name()).replace("+", "%20");
        return (attachment ? "attachment" : "inline") + "; filename=\"" + fileName + "\"; filename*=UTF-8''" + encoded;
    }

    public static Map.Entry<String, Long> readCrc32(String fileName) throws IOException {
        return readCrc32(new FileInputStream(new File(fileName)));
    }

    public static Map.Entry<String, Long> readCrc32(Path file) throws IOException {
        try (InputStream is = Files.newInputStream(file)) {
            return readCrc32(is);
        }
    }

    public static Map.Entry<String, Long> readCrc32(InputStream inputStream) throws IOException {
        try (CheckedInputStream cis = new CheckedInputStream(inputStream, new CRC32())) {
            String value = IOUtils.toString(cis, StandardCharsets.UTF_8);
            return Pair.of(value, cis.getChecksum().getValue());
        }
    }

    public static long copyWithCrc32(InputStream from, Path to) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(from, new CRC32());
        Files.createDirectories(to.getParent());
        Files.copy(checkedInputStream, to);
        return checkedInputStream.getChecksum().getValue();
    }

    public static String toString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public static byte[] toBytes(InputStream inputStream) throws IOException {
        return IOUtils.toByteArray(inputStream);
    }

    public static void delete(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (Exception ignore) { }
    }

    public static void deleteRecursively(Path path) throws IOException {
        try (Stream<Path> paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder()).forEach(IO::delete);
        }
    }

    static String extension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        String extension = FilenameUtils.getExtension(fileName);
        if (StringUtils.isBlank(extension)) {
            extension = FilenameUtils.getName(fileName);
        }
        return extension.toLowerCase();
    }
}
