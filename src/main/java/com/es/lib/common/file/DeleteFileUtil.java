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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 11.03.16
 */
public final class DeleteFileUtil {

    private DeleteFileUtil() { }

    public static boolean silent(File file) {
        try {
            return exceptional(file);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean exceptional(File file) {
        if (file == null) {
            throw new NullPointerException("file argument is null");
        }
        return file.delete();
    }

    public static void recursively(Path path) throws IOException {
        try (Stream<Path> paths = Files.walk(path)) {
            paths.map(Path::toFile)
                .sorted(Comparator.reverseOrder())
                .forEach(File::delete);
        }
    }
}
