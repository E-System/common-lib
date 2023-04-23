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
package com.eslibs.common.store;

import com.eslibs.common.file.FileName;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 01.02.16
 */
public interface IStore extends Serializable {

    String getFileName();

    String getFileExt();

    String getFullName();

    long getCrc32();

    long getSize();

    String getMime();

    String getAbbreviatedFileName(int maxWidth);

    boolean isImage();

    default String getUrl() {
        return null;
    }

    default boolean isOnlyLink() {
        return StringUtils.isNotBlank(getUrl());
    }

    static String fullName(IStore item) {
        return FileName.full(item.getFileName(), item.getFileExt());
    }

    static String abbreviatedFileName(IStore item, int maxWidth) {
        return FileName.abbreviated(item.getFileName(), item.getFileExt(), maxWidth);
    }

    static boolean isImage(IStore store) {
        return store != null && isImage(store.getMime());
    }

    static boolean isMime(String mime, String part) {
        return mime != null && mime.contains(part);
    }

    static boolean isImage(String mime) {
        return isMime(mime, "image");
    }
}
