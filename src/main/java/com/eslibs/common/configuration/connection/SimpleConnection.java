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

package com.eslibs.common.configuration.connection;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.net.InetSocketAddress;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.10.2023
 * Simple connection with host and port
 */
@Getter
@ToString
@SuperBuilder
@Jacksonized
public class SimpleConnection implements IConnection {

    protected final String host;
    protected final int port;

    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host, port);
    }
}