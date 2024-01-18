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
import lombok.ToString;

import java.nio.file.Path;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 10.02.2020
 */
@Getter
@ToString(callSuper = true)
public class FileData extends OutputData {

    private final Path relativePath;
    private final Path content;

    FileData(String fileName, Path relativePath, Path content) {
        super(fileName);
        this.relativePath = relativePath;
        this.content = content;
    }

    @Override
    public boolean isFile() {
        return true;
    }
}