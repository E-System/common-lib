/*
 * Copyright 2019 E-System LLC
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

package com.es.lib.common.email.simple;

import lombok.Getter;
import lombok.ToString;

import java.io.InputStream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.11.2019
 */
@Getter
@ToString
public class MessageStreamAttachment extends MessageAttachment {

    private InputStream inputStream;
    private String contentType;

    public MessageStreamAttachment(String name, InputStream inputStream, String contentType) {
        super(name);
        this.inputStream = inputStream;
        this.contentType = contentType;
    }
}
