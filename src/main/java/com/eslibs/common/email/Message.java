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
import com.eslibs.common.model.data.OutputData;
import lombok.Builder;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */

@Builder
public record Message(
    String recipients,
    String subject,
    String content,
    String fromAddress,
    String fromName,
    String replyTo,
    String carbonCopy,
    String blindCarbonCopy,
    Map<String, String> headers,
    Collection<Attachment> attachments,
    Map<String, String> extensions,
    Date sentDate,
    Date receivedDate
) {

    public boolean attachmentsAvailable() {
        return Items.isNotEmpty(attachments());
    }

    public Attachment rootAttachment() {
        return Items.collection(attachments).stream().filter(Attachment::root).findFirst().orElse(null);
    }

    public String id() {
        Attachment ra = rootAttachment();
        return ra != null ? ra.id() : null;
    }

    @Builder
    public record Attachment(
        OutputData data,
        String id,
        boolean root
    ) {

        public Attachment(OutputData data) {
            this(data, null);
        }

        public Attachment(OutputData data, String id) {
            this(data, id, false);
        }
    }
}
