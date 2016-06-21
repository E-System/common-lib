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

package com.es.lib.common.email;

import com.sun.mail.smtp.SMTPMessage;
import org.apache.commons.lang3.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 09.06.16
 */
public class SMTPMessageImpl extends SMTPMessage {

    private String messageId;

    public SMTPMessageImpl(Session session) {
        super(session);
    }

    public SMTPMessageImpl(Session session, InputStream is) throws MessagingException {
        super(session, is);
    }

    public SMTPMessageImpl(MimeMessage source) throws MessagingException {
        super(source);
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        if (StringUtils.isEmpty(messageId)) {
            super.updateMessageID();
        } else {
            setHeader("Message-ID", "<" + messageId + ">");
        }
    }

    @Override
    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        super.writeTo(os, null);
    }
}
