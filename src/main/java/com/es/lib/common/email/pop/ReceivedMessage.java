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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.mail.Address;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
@Getter
@ToString
@RequiredArgsConstructor
public class ReceivedMessage {

    private final Collection<Address> from;
    private final String subject;
    private final String message;
    private final Date sentDate;
    private final Map<String, String> headers;
    private final Map<String, File> attachments;

    public ReceivedMessage(Collection<Address> from, String subject, String message, Date sentDate, Map<String, String> headers) {
        this(from, subject, message, sentDate, headers, new HashMap<>());
    }
}
