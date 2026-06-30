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
package com.es.lib.common.file.output;

import com.es.lib.common.MimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.02.2020
 */
@Getter
@ToString
@RequiredArgsConstructor
public abstract class OutputData {

    private final String fileName;

    /**
     * Create output data from file path
     *
     * @param fileName     File name
     * @param relativePath Relative path (used for file store)
     * @param content      Path to file
     * @return File specific data
     */
    public static OutputData create(String fileName, String relativePath, Path content) {
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

    public boolean isFile() {
        return false;
    }

    public boolean isStream() {
        return false;
    }

    public boolean isBytes() {
        return false;
    }

    public DataInfo info(){
        if (isBytes()) {
            ByteData data = (ByteData) this;
            return new DataInfo(data.getFileName(), data.getContentType());
        } else if (isStream()) {
            StreamData data = (StreamData) this;
            return new DataInfo(data.getFileName(), data.getContentType());
        } else if (isFile()) {
            FileData data = (FileData) this;
            return new DataInfo(data.getFileName(), MimeUtil.get(data.getFileName()));
        }
        throw new IllegalArgumentException("Unsupported output data type");
    }

    public byte[] asBytes() throws IOException {
        if (isBytes()) {
            ByteData data = (ByteData) this;
            return data.getContent();
        } else if (isStream()) {
            StreamData data = (StreamData) this;
            return IOUtils.toByteArray(data.getContent());
        } else if (isFile()) {
            FileData data = (FileData) this;
            Path path = StringUtils.isNotBlank(data.getRelativePath()) ? Paths.get(data.getRelativePath()) : null;
            if (path != null) {
                path = path.resolve(data.getContent());
            }else{
                path = data.getContent();
            }
            return Files.readAllBytes(path);
        }
        throw new IllegalArgumentException("Unsupported output data type");
    }
}