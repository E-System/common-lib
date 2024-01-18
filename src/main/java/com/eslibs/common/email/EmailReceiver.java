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

import com.eslibs.common.configuration.IConfiguration;
import com.eslibs.common.exception.ESRuntimeException;
import com.eslibs.common.model.data.OutputData;
import com.eslibs.common.security.Hash;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
@Slf4j
public class EmailReceiver extends EmailProcessor {

    EmailReceiver(IConfiguration configuration) {
        super(Type.POP3, configuration);
    }

    @Override
    public Collection<com.eslibs.common.email.Message> fetch(Path pathToSave, boolean delete, Logger log) throws IOException, MessagingException {
        if (log == null) {
            log = EmailReceiver.log;
        }
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore("pop3");
            store.connect();
            inbox = store.getFolder("Inbox");
            inbox.open(delete ? Folder.READ_WRITE : Folder.READ_ONLY);

            // get the list of inbox messages
            jakarta.mail.Message[] messages = inbox.getMessages();

            if (messages.length == 0) {
                return Collections.emptyList();
            }

            Collection<com.eslibs.common.email.Message> result = new ArrayList<>(messages.length);
            for (jakarta.mail.Message message : messages) {
                Map<String, String> headers = getAllHeaders(message);

                Path realPathPrefix = pathToSave;
                String messageId = headers.get("Message-ID");
                if (StringUtils.isNotEmpty(messageId)) {
                    realPathPrefix = pathToSave.resolve(Hash.md5().of(messageId));
                }
                Collection<com.eslibs.common.email.Message.Attachment> attachments = new ArrayList<>(processAttachments(realPathPrefix, message));
                log.trace("Attachments: {}", attachments);

                result.add(
                    com.eslibs.common.email.Message.builder()
                        .recipients(Stream.of(message.getFrom()).map(Address::toString).collect(Collectors.joining(";")))
                        .subject(message.getSubject())
                        .content(fetchText(message))
                        .sentDate(message.getSentDate())
                        .receivedDate(message.getReceivedDate())
                        .headers(headers)
                        .attachments(attachments)
                        .build()
                );
                if (delete) {
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }
            return result;
        } finally {
            if (inbox != null) {
                try {
                    inbox.close();
                } catch (MessagingException e) {
                    log.warn(e.getMessage());
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn(e.getMessage());
                }

            }
        }
    }

    private Map<String, String> getAllHeaders(jakarta.mail.Message message) throws MessagingException {
        Map<String, String> result = new HashMap<>(10);
        Enumeration<Header> allHeaders = message.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = allHeaders.nextElement();
            result.put(
                header.getName(),
                header.getValue()
            );
        }
        return result;
    }

    private Collection<com.eslibs.common.email.Message.Attachment> processAttachments(Path pathToSave, Part part) throws IOException, MessagingException {
        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            return List.of(
                new com.eslibs.common.email.Message.Attachment(OutputData.create(
                    part.getFileName(),
                    pathToSave,
                    saveFile(pathToSave, part)
                ))
            );
        }
        if (part.isMimeType("text/*") || !(part.getContent() instanceof Multipart multiPart)) {
            return Collections.emptyList();
        }
        Collection<com.eslibs.common.email.Message.Attachment> result = new ArrayList<>(multiPart.getCount());
        for (int i = 0; i < multiPart.getCount(); ++i) {
            result.addAll(processAttachments(pathToSave, multiPart.getBodyPart(i)));
        }
        return result;
    }

    private String fetchText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            return (String) p.getContent();
        }

        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = fetchText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = fetchText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return fetchText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = fetchText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private Path saveFile(Path pathToSave, Part part) throws IOException, MessagingException {
        Path result = Files.createDirectories(pathToSave).resolve(part.getFileName());
        Files.copy(part.getInputStream(), result, StandardCopyOption.REPLACE_EXISTING);
        return result;
    }

    @Override
    public void send(com.eslibs.common.email.Message message, Logger log) throws IOException, MessagingException {
        throw new ESRuntimeException("Receiver not allow to send emails");
    }
}
