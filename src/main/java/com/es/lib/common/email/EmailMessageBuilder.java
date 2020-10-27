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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.05.16
 */
public class EmailMessageBuilder {

    private String backAddress;
    private String from;
    private String replyTo;
    private String destinations;
    private String subject;
    private String message;
    private Map<String, String> headers;
    private Collection<EmailAttachment> attachments;
    private Map<String, String> extensions;
    private String id;
    private EmailRootAttachment rootAttachment;

    EmailMessageBuilder() {
        this.attachments = new ArrayList<>();
        this.headers = new HashMap<>();
        this.extensions = new HashMap<>();
    }

    public static EmailMessageBuilder create() {
        return new EmailMessageBuilder();
    }

    public EmailMessageBuilder backAddress(String backAddress) {
        this.backAddress = backAddress;
        return this;
    }

    public EmailMessageBuilder from(String from) {
        this.from = from;
        return this;
    }

    public EmailMessageBuilder replyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public EmailMessageBuilder destinations(String destinations) {
        this.destinations = destinations;
        return this;
    }

    public EmailMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public EmailMessageBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public EmailMessageBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public EmailMessageBuilder attachments(Collection<EmailAttachment> attachments) {
        this.attachments = attachments;
        this.rootAttachment = null;
        return this;
    }

    public EmailMessageBuilder attachment(EmailAttachment attachment) {
        this.attachments.add(attachment);
        this.rootAttachment = null;
        return this;
    }

    public EmailMessageBuilder attachment(EmailRootAttachment attachment) {
        this.rootAttachment = attachment;
        this.attachments.clear();
        return this;
    }

    public EmailMessageBuilder extensions(Map<String, String> extensions) {
        this.extensions = extensions;
        return this;
    }

    public EmailMessageBuilder extension(String key, String value) {
        this.extensions.put(key, value);
        return this;
    }

    public EmailMessageBuilder id(String id) {
        this.id = id;
        return this;
    }

    public EmailMessage build() {
        return new EmailMessage(
            backAddress,
            from,
            replyTo,
            destinations,
            subject,
            message,
            headers,
            attachments,
            extensions,
            id,
            rootAttachment
        );
    }
}
