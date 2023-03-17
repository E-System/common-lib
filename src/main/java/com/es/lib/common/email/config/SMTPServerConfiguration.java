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

package com.es.lib.common.email.config;

import com.es.lib.common.security.model.Credentials;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.05.16
 */
public class SMTPServerConfiguration extends EmailServerConfiguration {

    public static final int DEFAULT_SSL_PORT = 465;
    public static final int DEFAULT_TLS_PORT = 587;

    public static final Map<String, EmailServer> PRESETS = new HashMap<>();

    static {
        PRESETS.put(
            "gmail",
            new EmailServer(
                "smtp.gmail.com",
                DEFAULT_SSL_PORT,
                true
            )
        );
        PRESETS.put(
            "gmail_tls",
            new EmailServer(
                "smtp.gmail.com",
                DEFAULT_TLS_PORT,
                false,
                true
            )
        );
        PRESETS.put(
            "mailru",
            new EmailServer(
                "smtp.mail.ru",
                DEFAULT_SSL_PORT,
                true
            )
        );
        PRESETS.put(
            "mailru_tls",
            new EmailServer(
                "smtp.mail.ru",
                DEFAULT_TLS_PORT,
                false,
                true
            )
        );
        PRESETS.put(
            "yandex",
            new EmailServer(
                "smtp.yandex.ru",
                DEFAULT_SSL_PORT,
                true
            )
        );

        PRESETS.put(
            "yandex_tls",
            new EmailServer(
                "smtp.yandex.ru",
                DEFAULT_TLS_PORT,
                false,
                true
            )
        );
        PRESETS.put(
            "e-system",
            new EmailServer(
                "mail.ext-system.com",
                DEFAULT_SSL_PORT,
                true
            )
        );
        PRESETS.put(
            "e-system_tls",
            new EmailServer(
                "mail.ext-system.com",
                DEFAULT_TLS_PORT,
                false,
                true
            )
        );
    }

    public SMTPServerConfiguration(String serverPreset, Credentials credentials) {
        super(EmailServer.Type.SMTP, PRESETS.get(serverPreset), credentials);
    }

    public SMTPServerConfiguration(EmailServer server, Credentials credentials) {
        super(EmailServer.Type.SMTP, server, credentials);
    }

    public SMTPServerConfiguration(String serverPreset, Credentials credentials, Map<String, Object> parameters, boolean debug) {
        super(EmailServer.Type.SMTP, PRESETS.get(serverPreset), credentials, parameters, debug);
    }

    public SMTPServerConfiguration(EmailServer server, Credentials credentials, Map<String, Object> parameters, boolean debug) {
        super(EmailServer.Type.SMTP, server, credentials, parameters, debug);
    }
}
