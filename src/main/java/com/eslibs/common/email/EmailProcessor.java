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


import com.eslibs.common.collection.Items;
import com.eslibs.common.configuration.IConfiguration;
import com.eslibs.common.configuration.SimpleConfiguration;
import com.eslibs.common.configuration.connection.IConnection;
import com.eslibs.common.configuration.connection.ServerConnection;
import com.eslibs.common.configuration.credentials.Credentials;
import com.eslibs.common.configuration.credentials.ICredentials;
import com.eslibs.common.security.Ssl;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
abstract class EmailProcessor implements IEmailProcessor, Serializable {

    public enum Type {
        SMTP,
        POP3
    }

    protected final Type type;
    protected final IConfiguration configuration;
    protected Session session;

    public EmailProcessor(Type type, IConfiguration configuration) {
        this.type = type;
        this.configuration = configuration;
        ServerConnection connection = getRealConnection();
        session = createSession(connection);
        session.setDebug(connection.isDebug());
    }

    protected Session createSession(ServerConnection connection) {
        return Session.getInstance(getProperty(type, connection), getAuthenticator());
    }

    protected String getLogin() {
        return getRealCredentials().getLogin();
    }

    protected Properties getProperty(Type transportType, ServerConnection connection) {
        Properties result = new Properties();
        String type = transportType.name().toLowerCase();
        result.put("mail.transport.protocol", type);
        result.put("mail.debug", connection.isDebug());
        result.put("mail.debug.auth", connection.isDebug());
        result.put("mail.host", connection.getHost());
        result.put("mail." + type + ".quitwait", false);
        result.put("mail." + type + ".auth", true);
        result.put("mail." + type + ".connectiontimeout", connection.getConnectTimeout());
        result.put("mail." + type + ".timeout", connection.getRwTimeout());
        result.put("mail." + type + ".port", connection.getPort());
        if (connection.getSsl() != null) {
            result.put("mail." + type + ".ssl.enable", true);
            result.put("mail." + type + ".socketFactory.port", connection.getPort());
            result.put("mail." + type + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            result.put("mail." + type + ".socketFactory.fallback", false);
            if (Ssl.Mode.TLS.equals(connection.getSsl())) {
                result.put("mail." + type + ".starttls.enable", true);
                result.put("mail." + type + ".starttls.required", true);
            }
            result.put("mail." + type + ".ssl.checkserveridentity", false);
        }
        result.putAll(Items.map(connection.getParams()));
        return result;
    }

    public Authenticator getAuthenticator() {
        final Credentials credentials = getRealCredentials();
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    credentials.getLogin(),
                    credentials.getPassword()
                );
            }
        };
    }

    protected SimpleConfiguration getRealConfiguration() {
        if (Objects.requireNonNull(configuration) instanceof SimpleConfiguration c) {
            return c;
        }
        throw new IllegalArgumentException("Unprocessable configuration type: " + configuration.getClass());
    }

    protected ServerConnection getRealConnection() {
        IConnection connection = getRealConfiguration().getConnection();
        if (Objects.requireNonNull(connection) instanceof ServerConnection c) {
            return c;
        }
        throw new IllegalArgumentException("Unprocessable server connection type: " + connection.getClass());
    }

    protected Credentials getRealCredentials() {
        ICredentials credentials = getRealConfiguration().getCredentials();
        if (Objects.requireNonNull(credentials) instanceof Credentials c) {
            return c;
        }
        throw new IllegalArgumentException("Unprocessable credentials type: " + credentials.getClass());
    }
}
