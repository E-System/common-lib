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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IO {

    public static FileType fileType(String fileName) {
        return FileTypeFinder.get(fileName);
    }

    public static String mime(String fileName) {
        return Mime.get(fileName);
    }

    public static String mime(Path file) {
        return mime(file.toString());
    }

    public static String fileNameDisposition(boolean attachment, String fileName) {
        return fileNameDisposition(attachment, fileName, StandardCharsets.UTF_8);
    }

    public static String fileNameDisposition(boolean attachment, String fileName, Charset charset) {
        return ContentDisposition.encode(attachment, fileName, charset);
    }

    public static Map.Entry<String, Long> readCrc32(String fileName) throws IOException {
        return readCrc32(Paths.get(fileName));
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

    public static FileInfo copy(InputStream from, Path to, boolean withCrc) throws IOException {
        InputStream source = from;
        if (withCrc) {
            source = new CheckedInputStream(from, new CRC32());
        }
        Files.createDirectories(to.getParent());
        long size = Files.copy(source, to);
        long crc32 = 0;
        if (withCrc) {
            crc32 = ((CheckedInputStream) source).getChecksum().getValue();
        }
        return new FileInfo(to, FileName.create(to), size, crc32, mime(to));
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
        } catch (Exception ignore) {
        }
    }

    public static void deleteRecursively(Path path) throws IOException {
        try (Stream<Path> paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder()).forEach(IO::delete);
        }
    }

    public static String humanReadableSize(long size, boolean is1024unit, int fraction) {
        long unit = is1024unit ? 1024 : 1000;
        if (size < unit) {
            return size + (size > 1 ? " Bytes" : " Byte");
        }
        long exp = (long) (Math.log(size) / Math.log(unit));
        double value = size / Math.pow(unit, exp);
        return String.format("%." + fraction + "f %s%s", value,
                             "KMGTPEZY".charAt((int) exp - 1), is1024unit ? "iB" : "B");
    }

    public static String humanReadableSize(long size, boolean is1024unit) {
        return humanReadableSize(size, is1024unit, 3);
    }

    public static String humanReadableSize(long size) {
        return humanReadableSize(size, true, 3);
    }

    public static Pair<Path, FileName> download(String url) {
        return download(url, null);
    }

    public static Pair<Path, FileName> download(String url, Function<String, FileName> fileNameCreator) {
        try {
            if (fileNameCreator == null) {
                fileNameCreator = FileName::create;
            }
            URL source = new URL(url);
            Path result = Files.createTempFile("download", "");
            FileUtils.copyURLToFile(source, result.toFile());
            return Pair.of(result, fileNameCreator.apply(source.getPath()));
        } catch (IOException e) {
            return null;
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
