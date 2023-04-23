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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@ToString
@AllArgsConstructor
public class EmailMessage {

    private final String fromAddress;
    private final String fromName;
    private final String replyTo;
    private final String destinations;
    private final String subject;
    private final String message;
    private final Map<String, String> headers;
    private final Collection<EmailAttachment> attachments;
    private final Map<String, String> extensions;
    private final String id;
    private final EmailRootAttachment rootAttachment;

    public boolean isAttachmentsAvailable() {
        return Items.isNotEmpty(getAttachments());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String destinations) {
        return builder().destinations(destinations);
    }

    public static Builder builder(String destinations, String subject, String message) {
        return builder()
            .destinations(destinations)
            .subject(subject)
            .message(message);
    }

    public static class Builder {

        private String fromAddress;
        private String fromName;
        private String replyTo;
        private String destinations;
        private String subject;
        private String message;
        private Map<String, String> headers;
        private Collection<EmailAttachment> attachments;
        private Map<String, String> extensions;
        private String id;
        private EmailRootAttachment rootAttachment;

        Builder() {
            this.attachments = new ArrayList<>();
            this.headers = new HashMap<>();
            this.extensions = new HashMap<>();
        }

        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public Builder fromName(String fromName) {
            this.fromName = fromName;
            return this;
        }

        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public Builder destinations(String destinations) {
            this.destinations = destinations;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder attachments(Collection<EmailAttachment> attachments) {
            this.attachments = attachments;
            this.rootAttachment = null;
            return this;
        }

        public Builder attachment(EmailAttachment attachment) {
            this.attachments.add(attachment);
            this.rootAttachment = null;
            return this;
        }

        public Builder rootAttachment(EmailRootAttachment rootAttachment) {
            this.rootAttachment = rootAttachment;
            this.attachments.clear();
            return this;
        }

        public Builder extensions(Map<String, String> extensions) {
            this.extensions = extensions;
            return this;
        }

        public Builder extension(String key, String value) {
            this.extensions.put(key, value);
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public EmailMessage build() {
            return new EmailMessage(
                fromAddress,
                fromName,
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
}
