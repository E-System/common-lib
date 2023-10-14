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

package com.eslibs.common.email;

import com.eslibs.common.configuration.IConfiguration;
import com.eslibs.common.configuration.SimpleConfiguration;
import com.eslibs.common.configuration.connection.ServerConnection;
import com.eslibs.common.configuration.credentials.ICredentials;
import com.eslibs.common.security.Ssl;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.10.15
 */
@Getter
@ToString
@SuperBuilder
public class Emails {

    public static final int DEFAULT_SSL_POP3_PORT = 995;
    public static final int DEFAULT_SSL_SMTP_PORT = 465;

    public static IEmailProcessor receiver(IConfiguration configuration) {
        return new EmailReceiver(configuration);
    }

    public static IEmailProcessor receiver(String name, ICredentials credentials) {
        return new EmailReceiver(POP3.get(name).credentials(credentials).build());
    }

    public static IEmailProcessor sender(IConfiguration configuration) {
        return new EmailSender(configuration);
    }

    public static IEmailProcessor sender(String name, ICredentials credentials) {
        return new EmailSender(SMTP.get(name).credentials(credentials).build());
    }

    public static final Map<String, SimpleConfiguration.SimpleConfigurationBuilder<?, ?>> POP3 = Map.of(
        "gmail", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("pop3.gmail.com")
                            .port(DEFAULT_SSL_POP3_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "mailru", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("pop3.mail.ru")
                            .port(DEFAULT_SSL_POP3_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "yandex", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("pop3.yandex.ru")
                            .port(DEFAULT_SSL_POP3_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "e-system", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("pop.ext-system.com")
                            .port(DEFAULT_SSL_POP3_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build())
    );

    public static final Map<String, SimpleConfiguration.SimpleConfigurationBuilder<?, ?>> SMTP = Map.of(
        "gmail", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("smtp.gmail.com")
                            .port(DEFAULT_SSL_SMTP_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "mailru", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("smtp.mail.ru")
                            .port(DEFAULT_SSL_SMTP_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "yandex", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("smtp.yandex.ru")
                            .port(DEFAULT_SSL_SMTP_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build()),

        "e-system", SimpleConfiguration.builder()
            .connection(ServerConnection.builder()
                            .host("smtp.ext-system.com")
                            .port(DEFAULT_SSL_SMTP_PORT)
                            .ssl(Ssl.Mode.SSL)
                            .build())
    );
}
