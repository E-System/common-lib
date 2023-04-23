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

package com.eslibs.common.email.common;


import com.eslibs.common.email.config.EmailServerConfiguration;
import jakarta.mail.Session;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
public class BaseEmailProcessor implements Serializable {

    protected final EmailServerConfiguration config;
    protected Session session;

    public BaseEmailProcessor(EmailServerConfiguration config) throws IOException {
        this.config = config;
        session = createSession();
        session.setDebug(config.isDebug());
    }

    protected Session createSession() throws IOException {
        return Session.getInstance(config.getProperty(), config.getAuthenticator());
    }

    protected String getLogin() {
        return config.getCredentials().getLogin();
    }
}
