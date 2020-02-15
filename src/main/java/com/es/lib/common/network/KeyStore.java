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
package com.es.lib.common.network;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 15.02.2020
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyStore {

    private Path path;
    private String storePassword;
    private String keyPassword;
    private String type;

    public static KeyStore create(Path path, String storePassword, String keyPassword) {
        String extension = FilenameUtils.getExtension(path.toString());
        String type;
        switch (extension.toLowerCase()) {
            case "jks":
                type = "JKS";
                break;
            case "p12":
            case "pfx":
                type = "PKCS12";
                break;
            default:
                type = null;
        }
        if (type == null) {
            throw new IllegalArgumentException("Unable to determine key type by file: " + path.toString());
        }
        return create(path, storePassword, keyPassword, type);
    }

    public static KeyStore create(Path path, String storePassword, String keyPassword, String type) {
        return new KeyStore(path, storePassword, keyPassword, type);
    }
}
