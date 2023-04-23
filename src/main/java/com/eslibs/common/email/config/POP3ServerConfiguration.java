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

package com.eslibs.common.email.config;

import com.eslibs.common.security.model.Credentials;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.05.16
 */
public class POP3ServerConfiguration extends EmailServerConfiguration {

    public static final int DEFAULT_SSL_PORT = 995;

    public static final Map<String, EmailServer> PRESETS = new HashMap<>();

    static {
        PRESETS.put(
            "gmail",
            new EmailServer(
                "pop3.gmail.com",
                DEFAULT_SSL_PORT,
                true
            )
        );
        PRESETS.put(
            "mailru",
            new EmailServer(
                "pop3.mail.ru",
                DEFAULT_SSL_PORT,
                true
            )
        );
        PRESETS.put(
            "yandex",
            new EmailServer(
                "pop3.yandex.ru",
                DEFAULT_SSL_PORT,
                true
            )
        );
    }

    public POP3ServerConfiguration(EmailServer parameter, Credentials credentials) {
        super(EmailServer.Type.POP3, parameter, credentials);
    }

    public POP3ServerConfiguration(EmailServer server, Credentials credentials, Map<String, Object> parameters, boolean debug) {
        super(EmailServer.Type.POP3, server, credentials, parameters, debug);
    }
}
