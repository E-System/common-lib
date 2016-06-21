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

package com.es.lib.common.email.pop;

import javax.mail.Address;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
public class ReceivedMessage {

    private Collection<Address> from;
    private String subject;
    private String message;
    private Date sentDate;
    private Map<String, String> headers;
    private Map<String, File> attachments;

    public ReceivedMessage(Collection<Address> from, String subject, String message, Date sentDate, Map<String, String> headers) {
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.sentDate = sentDate;
        this.headers = headers;
    }

    public ReceivedMessage(Collection<Address> from, String subject, String message, Date sentDate, Map<String, String> headers, Map<String, File> attachments) {
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.sentDate = sentDate;
        this.headers = headers;
        this.attachments = attachments;
    }

    public Collection<Address> getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, File> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return "ReceivedMessage{" +
               "from=" + from +
               ", subject='" + subject + '\'' +
               ", message='" + message + '\'' +
               ", sentDate=" + sentDate +
               ", headers=" + headers +
               ", attachments=" + attachments +
               '}';
    }
}
