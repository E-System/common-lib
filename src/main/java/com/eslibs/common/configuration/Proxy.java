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
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Proxy extends SimpleConfiguration {

    @Builder.Default
    protected final Type type = Type.HTTP;

    public java.net.Proxy proxy() {
        return switch (Objects.requireNonNull(connection)) {
            case SimpleConnection c -> new java.net.Proxy(type.type(), c.inetSocketAddress());
            default ->
                throw new IllegalArgumentException("Unprocessable server connection type: " + connection.getClass());
        };
    }

    public enum Type {
        DIRECT,
        HTTP,
        SOCKS;

        @JsonIgnore
        public java.net.Proxy.Type type() {
            return switch (this) {
                case DIRECT -> java.net.Proxy.Type.DIRECT;
                case HTTP -> java.net.Proxy.Type.HTTP;
                case SOCKS -> java.net.Proxy.Type.SOCKS;
            };
        }
    }
}
