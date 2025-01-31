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

import com.eslibs.common.configuration.Proxy;
import com.eslibs.common.security.Ssl;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.10.2023
 * Server connection
 */
@Getter
@ToString
@Jacksonized
@SuperBuilder
public class ServerConnection extends SimpleConnection {

    @Builder.Default
    protected final Ssl.Mode ssl = null;
    @Builder.Default
    private final long connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    @Builder.Default
    private final long rwTimeout = DEFAULT_RW_TIMEOUT;
    @Builder.Default
    private final Proxy proxy = null;
    @Builder.Default
    private final Map<String, Object> params = new HashMap<>();
    @Builder.Default
    private final boolean debug = false;
}
