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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;

@Getter
@ToString
@RequiredArgsConstructor
public class FileName {

    private final String name;
    private final String ext;

    public String getFullName() {
        return full(name, ext);
    }

    public String getAbbreviated(int maxWidth) {
        return abbreviated(name, ext, maxWidth);
    }

    public static String full(String name, String ext) {
        return name + "." + ext;
    }

    public static String abbreviated(String name, String ext, int maxWidth) {
        int extSize = ext.length();
        int nameSize = name.length();
        if ((nameSize + extSize + 1) < maxWidth) {
            return full(name, ext);
        }
        return full(StringUtils.abbreviateMiddle(name, "..", maxWidth - extSize - 1), ext);
    }

    public static FileName create(String name, String ext) {
        return new FileName(name, ext);
    }

    public static FileName create(String fileName) {
        return create(fileName, true);
    }

    public static FileName create(String fileName, boolean lowerExt) {
        String ext = FilenameUtils.getExtension(fileName);
        return create(
            FilenameUtils.getBaseName(fileName),
            lowerExt ? ext.toLowerCase() : ext
        );
    }

    public static FileName create(Path file) {
        return create(file.toString());
    }
}