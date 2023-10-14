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
package com.eslibs.common.configuration;

import com.eslibs.common.configuration.connection.SimpleConnection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 15.02.2020
 */
@Getter
@ToString
@SuperBuilder
@Jacksonized
public class Proxy extends SimpleConfiguration {

    @Builder.Default
    protected final java.net.Proxy.Type type = java.net.Proxy.Type.HTTP;

    public java.net.Proxy proxy() {
        if (Objects.requireNonNull(connection) instanceof SimpleConnection c) {
            return new java.net.Proxy(type, c.inetSocketAddress());
        }
        throw new IllegalArgumentException("Unprocessable server connection type: " + connection.getClass());
    }
}
