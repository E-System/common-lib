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

import com.es.lib.common.collection.CollectionUtil;

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
public class EmailServerConfiguration implements Serializable {

    private final EmailServerType serverType;
    private final EmailServer server;
    private final EmailAuth auth;
    private final Map<String, Object> parameters;
    private final boolean debug;

    EmailServerConfiguration(EmailServerType serverType, EmailServer server, EmailAuth auth) {
        this(serverType, server, auth, new HashMap<>(), false);
    }

    EmailServerConfiguration(EmailServerType serverType, EmailServer server, EmailAuth auth, Map<String, Object> parameters, boolean debug) {
        this.serverType = serverType;
        this.server = server;
        this.auth = auth;
        this.parameters = parameters;
        this.debug = debug;
    }

    public EmailServerType getServerType() {
        return serverType;
    }

    public EmailServer getServer() {
        return server;
    }

    public EmailAuth getAuth() {
        return auth;
    }

    public boolean isDebug() {
        return debug;
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
                    getAuth().getLogin(),
                    getAuth().getPassword()
                );
            }
        };
    }

    private String formatParameters() {
        if (CollectionUtil.isEmpty(parameters)) {
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

    @Override
    public String toString() {
        return "EmailServerConfiguration{" +
               "serverType=" + serverType +
               ", server=" + server +
               ", auth=" + auth +
               '}';
    }
}
