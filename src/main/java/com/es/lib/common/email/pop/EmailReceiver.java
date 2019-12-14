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

import com.es.lib.common.HashUtil;
import com.es.lib.common.email.common.BaseEmailProcessor;
import com.es.lib.common.email.config.POP3ServerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
@Slf4j
public class EmailReceiver extends BaseEmailProcessor {

    public EmailReceiver(POP3ServerConfiguration configuration) throws IOException {
        super(configuration);
    }

    public Collection<ReceivedMessage> getAll(String pathPrefix, boolean deleteCollected) throws MessagingException, IOException {
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore("pop3");
            store.connect();
            inbox = store.getFolder("Inbox");
            inbox.open(deleteCollected ? Folder.READ_WRITE : Folder.READ_ONLY);

            // get the list of inbox messages
            Message[] messages = inbox.getMessages();

            if (messages.length == 0) {
                return Collections.emptyList();
            }

            Collection<ReceivedMessage> result = new ArrayList<>(messages.length);
            for (Message message : messages) {
                Map<String, String> headers = getAllHeaders(message);

                String realPathPrefix = pathPrefix;
                String messageId = headers.get("Message-ID");
                if (StringUtils.isNotEmpty(messageId)) {
                    realPathPrefix += "/" + HashUtil.md5(messageId);
                }
                Map<String, File> attachments = new HashMap<>(processAttachments(realPathPrefix, message));
                log.trace("Attachments: {}", attachments);

                result.add(
                    new ReceivedMessage(
                        Arrays.asList(message.getFrom()),
                        message.getSubject(),
                        fetchText(message),
                        message.getSentDate(),
                        headers,
                        attachments
                    )
                );
                if (deleteCollected) {
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }
            return result;
        } finally {
            if (inbox != null) {
                try {
                    inbox.close(true);
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

    private Map<String, String> getAllHeaders(Message message) throws MessagingException {
        Map<String, String> result = new HashMap<>(10);
        Enumeration allHeaders = message.getAllHeaders();
        while (allHeaders.hasMoreElements()) {
            Header header = (Header) allHeaders.nextElement();
            result.put(
                header.getName(),
                header.getValue()
            );
        }
        return result;
    }

    private Map<String, File> processAttachments(String pathPrefix, Part part) throws IOException, MessagingException {
        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            Map<String, File> result = new HashMap<>(1);
            result.put(
                part.getFileName(),
                saveFile(pathPrefix, part)
            );
            return result;
        }
        if (part.isMimeType("text/*") || !(part.getContent() instanceof Multipart)) {
            return Collections.emptyMap();
        }
        Multipart multiPart = (Multipart) part.getContent();
        Map<String, File> result = new HashMap<>(multiPart.getCount());
        for (int i = 0; i < multiPart.getCount(); ++i) {
            result.putAll(processAttachments(pathPrefix, multiPart.getBodyPart(i)));
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

    private File saveFile(String pathPrefix, Part part) throws IOException, MessagingException {
        Path destinationFile = Files.createDirectories(Paths.get(pathPrefix)).resolve(part.getFileName());
        Files.copy(part.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return destinationFile.toFile();
    }
}
