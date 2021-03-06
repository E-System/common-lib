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
package com.es.lib.common.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.net.InetSocketAddress;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 15.02.2020
 */
@Getter
@ToString
@RequiredArgsConstructor
public class Proxy {

    private final java.net.Proxy.Type type;
    private final String host;
    private final int port;

    public Proxy(String host, int port) {
        this(java.net.Proxy.Type.HTTP, host, port);
    }

    public java.net.Proxy toNative() {
        return new java.net.Proxy(getType(), new InetSocketAddress(getHost(), getPort()));
    }
}
