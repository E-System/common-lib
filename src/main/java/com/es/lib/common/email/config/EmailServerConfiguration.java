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

import com.es.lib.common.collection.Items;
import com.es.lib.common.security.model.Credentials;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.10.15
 */
@Getter
@ToString
@RequiredArgsConstructor
public class EmailServerConfiguration implements Serializable {

    private final EmailServer.Type serverType;
    private final EmailServer server;
    private final Credentials credentials;
    private final Map<String, Object> parameters;
    private final boolean debug;

    EmailServerConfiguration(EmailServer.Type serverType, EmailServer server, Credentials credentials) {
        this(serverType, server, credentials, new HashMap<>(), false);
    }

    public Properties getProperty() throws IOException {
        String type = getTypeAsString();
        return load(
            formatRow("mail.transport.protocol", type) +
            formatRow("mail.host", server.getHost()) +
            formatRow("mail." + type + ".auth", true) +
            formatRow("mail." + type + ".timeout", server.getTimeout()) +
            formatRow("mail." + type + ".port", server.getPort()) +
            formatSslOrTls() +
            formatDebug() +
            formatRow("mail." + type + ".quitwait", false) +
            formatParameters()
        );
    }

    public Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    getCredentials().getLogin(),
                    getCredentials().getPassword()
                );
            }
        };
    }

    private String formatParameters() {
        if (Items.isEmpty(parameters)) {
            return "\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            sb.append(formatRow(entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    private String formatSslOrTls() {
        if (!server.isSsl() && !server.isTls()) {
            return "\n";
        }
        if (server.isSsl()) {
            String type = getTypeAsString();
            return formatRow("mail." + type + ".ssl.enable", true) +
                   formatRow("mail." + type + ".socketFactory.port", server.getPort()) +
                   formatRow("mail." + type + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory") +
                   formatRow("mail." + type + ".socketFactory.fallback", false);
        }
        String type = getTypeAsString();
        return formatRow("mail." + type + ".starttls.enable", true) +
               formatRow("mail." + type + ".starttls.required", true);
    }

    private String formatDebug() {
        if (!isDebug()) {
            return "\n";
        }
        return formatRow("mail.debug", true);
    }

    private String getTypeAsString() {
        return serverType.toString().toLowerCase();
    }

    private String formatRow(String key, Object value) {
        return key + "=" + value + "\n";
    }

    private Properties load(String content) throws IOException {
        Properties result = new Properties();
        result.load(
            new StringReader(
                content
            )
        );
        return result;
    }
}
