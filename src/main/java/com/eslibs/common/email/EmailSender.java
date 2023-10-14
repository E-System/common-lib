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
import com.eslibs.common.configuration.IConfiguration;
import com.eslibs.common.exception.ESRuntimeException;
import com.eslibs.common.model.data.ByteData;
import com.eslibs.common.model.data.FileData;
import com.eslibs.common.model.data.StreamData;
import com.eslibs.common.model.data.TypedData;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.angus.mail.smtp.SMTPMessage;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import static jakarta.mail.Part.ATTACHMENT;


/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
public class EmailSender extends EmailProcessor {

    EmailSender(IConfiguration configuration) {
        super(Type.SMTP, configuration);
    }

    @Override
    public void send(com.eslibs.common.email.Message message, Logger log) throws IOException, MessagingException {
        if (log == null) {
            log = EmailSender.log;
        }
        Transport.send(createMessage(message, log));
    }

    private jakarta.mail.Message createMessage(com.eslibs.common.email.Message emailMessage, Logger log) throws MessagingException, IOException {
        SMTPMessage smtpMessage = new SMTPMessageImpl(session, emailMessage.id());

        String fromAddress = StringUtils.isEmpty(emailMessage.fromAddress()) ? null : emailMessage.fromAddress();
        String fromName = StringUtils.isEmpty(emailMessage.fromName()) ? null : emailMessage.fromName();
        log.trace("From address: {}; From name: {}", fromAddress, fromName);
        InternetAddress fromInetAddress = getInternetAddress(fromAddress, fromName);
        smtpMessage.setFrom(fromInetAddress);
        if (StringUtils.isNotEmpty(emailMessage.replyTo())) {
            smtpMessage.setReplyTo(InternetAddress.parse(emailMessage.replyTo()));
        }

        InternetAddress sender = new InternetAddress(fromAddress != null ? fromAddress : getLogin());
        log.trace("Sender: {}", sender);
        smtpMessage.setEnvelopeFrom(sender.getAddress());
        smtpMessage.setSender(sender);

        smtpMessage.setRecipients(jakarta.mail.Message.RecipientType.TO, emailMessage.recipients());
        if (StringUtils.isNotBlank(emailMessage.carbonCopy())) {
            smtpMessage.setRecipients(jakarta.mail.Message.RecipientType.CC, emailMessage.carbonCopy());
        }
        if (StringUtils.isNotBlank(emailMessage.blindCarbonCopy())) {
            smtpMessage.setRecipients(jakarta.mail.Message.RecipientType.BCC, emailMessage.blindCarbonCopy());
        }
        smtpMessage.setSubject(emailMessage.subject());

        processHeaders(emailMessage, smtpMessage);

        generateBody(emailMessage, smtpMessage);

        processExtensions(emailMessage, smtpMessage, log);

        return smtpMessage;
    }

    private InternetAddress getInternetAddress(String fromAddress, String fromName) throws UnsupportedEncodingException, AddressException {
        if (fromAddress != null) {
            if (fromName != null) {
                return new InternetAddress(fromAddress, fromName);
            }
            return new InternetAddress(fromAddress);
        }
        if (fromName != null) {
            return new InternetAddress(getLogin(), fromName);
        }
        return new InternetAddress(getLogin());
    }

    private void generateBody(com.eslibs.common.email.Message emailMessage, SMTPMessage message) throws MessagingException, IOException {
        com.eslibs.common.email.Message.Attachment rootAttachment = emailMessage.rootAttachment();
        if (rootAttachment != null) {
            ByteArrayDataSource dataSource;
            if (rootAttachment.data().isBytes() || rootAttachment.data().isStream()) {
                if (rootAttachment.data().isBytes()) {
                    ByteData data = (ByteData) rootAttachment.data();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                } else {
                    StreamData data = (StreamData) rootAttachment.data();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                }
                if (StringUtils.isNotEmpty(rootAttachment.data().getFileName())) {
                    dataSource.setName(rootAttachment.data().getFileName());
                }
                message.setContent(dataSource, ((TypedData) rootAttachment.data()).getContentType());
            } else {
                FileData data = (FileData) rootAttachment.data();

                FileDataSource fds = new FileDataSource(data.getContent().toFile());
                message.setDataHandler(new DataHandler(fds));
                message.setFileName(StringUtils.isNotEmpty(data.getFileName()) ? data.getFileName() : fds.getName());
                message.setDisposition(ATTACHMENT);
            }
        } else if (Items.isEmpty(emailMessage.attachments())) {
            message.setContent(emailMessage.content(), "text/html; charset=UTF-8");
        } else if (StringUtils.isNotEmpty(emailMessage.content())) {
            Multipart multipart = new MimeMultipart("related");

            multipart.addBodyPart(createMessageBodyPart(emailMessage));

            processAttachments(emailMessage, multipart);

            message.setContent(multipart);
        }
    }

    private void processAttachments(com.eslibs.common.email.Message emailMessage, Multipart multipart) throws MessagingException, IOException {
        for (com.eslibs.common.email.Message.Attachment attachment : emailMessage.attachments()) {
            BodyPart mimeBodyPart = new MimeBodyPart();

            DataSource dataSource;
            if (attachment.data().isBytes() || attachment.data().isStream()) {
                if (attachment.data().isBytes()) {
                    ByteData data = (ByteData) attachment.data();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                } else {
                    StreamData data = (StreamData) attachment.data();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                }
            } else {
                FileData content = (FileData) attachment.data();
                dataSource = new FileDataSource(content.getContent().toFile());
            }
            mimeBodyPart.setDataHandler(new DataHandler(dataSource));
            if (StringUtils.isNotBlank(attachment.data().getFileName())) {
                mimeBodyPart.setFileName(attachment.data().getFileName());
            }

            if (attachment.id() != null) {
                mimeBodyPart.setHeader("Content-ID", "<" + attachment.id() + ">");
            }

            multipart.addBodyPart(mimeBodyPart);
        }
    }

    private BodyPart createMessageBodyPart(com.eslibs.common.email.Message emailMessage) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailMessage.content(), "text/html; charset=UTF-8");
        return bodyPart;
    }

    private void processHeaders(com.eslibs.common.email.Message emailMessage, SMTPMessage message) throws MessagingException {
        if (Items.isNotEmpty(emailMessage.headers())) {
            for (Map.Entry<String, String> entry : emailMessage.headers().entrySet()) {
                message.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private void processExtensions(com.eslibs.common.email.Message emailMessage, SMTPMessage message, Logger log) throws MessagingException, IOException {
        if (Items.isNotEmpty(emailMessage.extensions())) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : emailMessage.extensions().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("SIZE")) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                    message.writeTo(baos);
                    int size = baos.toByteArray().length;
                    if (size > 0) {
                        size += ("Content-Length: " + size + "\r\n").length();
                        value = String.valueOf(size);
                        message.setHeader("Content-Length", value);
                        log.trace("Find SIZE extension. Calculate: {}", value);
                    }

                    baos.close();
                }
                sb.append(key).append("=").append(value).append(" ");
            }
            message.setMailExtension(sb.toString());
        }
    }

    @Override
    public Collection<com.eslibs.common.email.Message> fetch(Path pathToSave, boolean delete, Logger log) throws IOException, MessagingException {
        throw new ESRuntimeException("Sender not allow to fetch emails");
    }

    private static class SMTPMessageImpl extends SMTPMessage {

        private final String id;

        public SMTPMessageImpl(Session session, String id) {
            super(session);
            this.id = id;
        }


        @Override
        protected void updateMessageID() throws MessagingException {
            if (StringUtils.isEmpty(id)) {
                super.updateMessageID();
            } else {
                setHeader("Message-ID", "<" + id + ">");
            }
        }
    }
}
