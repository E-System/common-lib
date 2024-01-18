/*
 * Copyright 2020 E-System LLC
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
package com.eslibs.common.model.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.02.2020
 */
@Getter
@ToString
@RequiredArgsConstructor
public abstract class OutputData {

    private final String fileName;

    public boolean isFile() {
        return false;
    }

    public boolean isStream() {
        return false;
    }

    public boolean isBytes() {
        return false;
    }

    /**
     * Create output data from file path
     *
     * @param fileName     File name
     * @param relativePath Relative path (used for file store)
     * @param content      Path to file
     * @return File specific data
     */
    public static OutputData create(String fileName, Path relativePath, Path content) {
        return new FileData(fileName, relativePath, content);
    }

    /**
     * Create output data from input stream
     *
     * @param fileName    File name
     * @param contentType Content type
     * @param content     Input stream
     * @return File specific data
     */
    public static OutputData create(String fileName, String contentType, InputStream content) {
        return new StreamData(fileName, contentType, content);
    }

    /**
     * Create output data from byte array
     *
     * @param fileName    File name
     * @param contentType Content type
     * @param content     Byte array
     * @return File specific data
     */
    public static OutputData create(String fileName, String contentType, byte[] content) {
        return new ByteData(fileName, contentType, content);
    }
}