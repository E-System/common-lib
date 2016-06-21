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

import java.util.Collection;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class EmailMessage {

    private final String backAddress;
    private final String from;
    private final String destinations;
    private final String subject;
    private final String message;
    private final Map<String, String> headers;
    private final Collection<EmailAttachment> attachments;
    private final Map<String, String> extensions;
    private final String id;
    private final EmailRootAttachment rootAttachment;

    public static EmailMessageBuilder create() {
        return new EmailMessageBuilder();
    }

    public static EmailMessageBuilder create(String destinations) {
        return create().destinations(destinations);
    }

    public static EmailMessageBuilder create(String destinations, String subject, String message) {
        return create()
                .destinations(destinations)
                .subject(subject)
                .message(message);
    }

    EmailMessage(String backAddress, String from, String destinations, String subject, String message, Map<String, String> headers, Collection<EmailAttachment> attachments, Map<String, String> extensions, String id, EmailRootAttachment rootAttachment) {
        this.backAddress = backAddress;
        this.from = from;
        this.destinations = destinations;
        this.subject = subject;
        this.message = message;
        this.headers = headers;
        this.attachments = attachments;
        this.extensions = extensions;
        this.id = id;
        this.rootAttachment = rootAttachment;
    }

    public String getBackAddress() {
        return backAddress;
    }

    public String getFrom() {
        return from;
    }

    public String getDestinations() {
        return destinations;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Collection<EmailAttachment> getAttachments() {
        return attachments;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public String getId() {
        return id;
    }

    public EmailRootAttachment getRootAttachment() {
        return rootAttachment;
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
               "backAddress='" + backAddress + '\'' +
               ", from='" + from + '\'' +
               ", destinations='" + destinations + '\'' +
               ", subject='" + subject + '\'' +
               ", message='" + message + '\'' +
               ", headers=" + headers +
               ", attachments=" + attachments +
               ", extensions=" + extensions +
               ", id='" + id + '\'' +
               ", rootAttachment=" + rootAttachment +
               '}';
    }
}
