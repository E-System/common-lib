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
    }

    public SMTPServerConfiguration(EmailServer server, EmailAuth auth) {
        super(EmailServerType.SMTP, server, auth);
    }

    public SMTPServerConfiguration(EmailServer server, EmailAuth auth, Map<String, Object> parameters, boolean debug) {
        super(EmailServerType.SMTP, server, auth, parameters, debug);
    }

    public SMTPServerConfiguration(EmailServerType serverType, EmailServer server, EmailAuth auth) {
        super(serverType, server, auth);
    }

    public SMTPServerConfiguration(EmailServerType serverType, EmailServer server, EmailAuth auth, Map<String, Object> parameters, boolean debug) {
        super(serverType, server, auth, parameters, debug);
    }
}
