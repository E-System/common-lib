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

import com.es.lib.common.collection.Items;
import com.es.lib.common.email.common.BaseEmailProcessor;
import com.es.lib.common.email.config.SMTPServerConfiguration;
import com.es.lib.common.model.data.ByteData;
import com.es.lib.common.model.data.FileData;
import com.es.lib.common.model.data.StreamData;
import com.es.lib.common.model.data.TypedData;
import com.sun.mail.smtp.SMTPMessage;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static jakarta.mail.Part.ATTACHMENT;


/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
public class EmailSender extends BaseEmailProcessor {

    public EmailSender(SMTPServerConfiguration configuration) throws IOException {
        super(configuration);
    }

    private Message createMessage(EmailMessage emailMessage) throws MessagingException, IOException {
        SMTPMessage smtpMessage = createSMTPMessage(emailMessage);

        String fromAddress = StringUtils.isEmpty(emailMessage.getFromAddress()) ? null : emailMessage.getFromAddress();
        String fromName = StringUtils.isEmpty(emailMessage.getFromName()) ? null : emailMessage.getFromName();
        log.trace("From address: {}; From name: {}", fromAddress, fromName);
        InternetAddress fromInetAddress;
        if (fromAddress != null) {
            if (fromName != null) {
                fromInetAddress = new InternetAddress(fromAddress, fromName);
            } else {
                fromInetAddress = new InternetAddress(fromAddress);
            }
        } else {
            if (fromName != null) {
                fromInetAddress = new InternetAddress(getLogin(), fromName);
            } else {
                fromInetAddress = new InternetAddress(getLogin());
            }
        }
        smtpMessage.setFrom(fromInetAddress);
        if (StringUtils.isNotEmpty(emailMessage.getReplyTo())) {
            smtpMessage.setReplyTo(InternetAddress.parse(emailMessage.getReplyTo()));
        }

        InternetAddress sender = new InternetAddress(fromAddress != null ? fromAddress : getLogin());
        log.trace("Sender: {}", sender);
        smtpMessage.setEnvelopeFrom(sender.getAddress());
        smtpMessage.setSender(sender);

        smtpMessage.setRecipients(Message.RecipientType.TO, emailMessage.getDestinations());
        if (StringUtils.isNotBlank(emailMessage.getCarbonCopy())) {
            smtpMessage.setRecipients(Message.RecipientType.CC, emailMessage.getCarbonCopy());
        }
        if (StringUtils.isNotBlank(emailMessage.getBlindCarbonCopy())) {
            smtpMessage.setRecipients(Message.RecipientType.BCC, emailMessage.getBlindCarbonCopy());
        }
        smtpMessage.setSubject(emailMessage.getSubject());

        processHeaders(emailMessage, smtpMessage);

        generateBody(emailMessage, smtpMessage);

        processExtensions(emailMessage, smtpMessage);

        return smtpMessage;
    }

    private SMTPMessage createSMTPMessage(EmailMessage emailMessage) {
        if (StringUtils.isBlank(emailMessage.getId()) && emailMessage.getRootAttachment() == null) {
            return new SMTPMessage(session);
        }
        SMTPMessageImpl result = new SMTPMessageImpl(session);
        result.setMessageId(emailMessage.getId());
        return result;
    }

    private void generateBody(EmailMessage emailMessage, SMTPMessage message) throws MessagingException, IOException {
        if (emailMessage.getRootAttachment() != null) {
            EmailRootAttachment attachment = emailMessage.getRootAttachment();
            if (attachment.getData().isBytes() || attachment.getData().isStream()) {
                ByteArrayDataSource byteArrayDataSource;
                if (attachment.getData().isBytes()) {
                    ByteData data = (ByteData) attachment.getData();
                    byteArrayDataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                } else {
                    StreamData data = (StreamData) attachment.getData();
                    byteArrayDataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                }
                if (StringUtils.isNotEmpty(attachment.getData().getFileName())) {
                    byteArrayDataSource.setName(attachment.getData().getFileName());
                }
                message.setContent(byteArrayDataSource, ((TypedData) attachment.getData()).getContentType());
            } else {
                FileData data = (FileData) attachment.getData();

                FileDataSource fds = new FileDataSource(data.getContent().toFile());
                message.setDataHandler(new DataHandler(fds));
                message.setFileName(StringUtils.isNotEmpty(data.getFileName()) ? data.getFileName() : fds.getName());
                message.setDisposition(ATTACHMENT);
            }
        } else if (Items.isEmpty(emailMessage.getAttachments())) {
            message.setContent(emailMessage.getMessage(), "text/html; charset=UTF-8");
        } else if (StringUtils.isNotEmpty(emailMessage.getMessage())) {
            Multipart multipart = new MimeMultipart("related");

            multipart.addBodyPart(createMessageBodyPart(emailMessage));

            processAttachments(emailMessage, multipart);

            message.setContent(multipart);
        }
    }

    private void processAttachments(EmailMessage emailMessage, Multipart multipart) throws MessagingException, IOException {
        for (EmailAttachment attachment : emailMessage.getAttachments()) {
            BodyPart mimeBodyPart = new MimeBodyPart();

            DataSource dataSource;
            if (attachment.getData().isBytes() || attachment.getData().isStream()) {
                if (attachment.getData().isBytes()) {
                    ByteData data = (ByteData) attachment.getData();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                } else {
                    StreamData data = (StreamData) attachment.getData();
                    dataSource = new ByteArrayDataSource(data.getContent(), data.getContentType());
                }

                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                //  mimeBodyPart.addHeader("Content-Transfer-Encoding", "base64");
                // mimeBodyPart.addHeader("Content-Type", ((TypedData) attachment.getData()).getContentType() + "; charset=utf-8");
                if (StringUtils.isNotBlank(attachment.getData().getFileName())) {
                    mimeBodyPart.setFileName(attachment.getData().getFileName());
                }
            } else {
                FileData content = (FileData) attachment.getData();
                dataSource = new FileDataSource(content.getContent().toFile());
                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                mimeBodyPart.setFileName(content.getFileName());
            }


            if (attachment.getCid() != null) {
                mimeBodyPart.setHeader("Content-ID", "<" + attachment.getCid() + ">");
            }

            multipart.addBodyPart(mimeBodyPart);
        }
    }

    private BodyPart createMessageBodyPart(EmailMessage emailMessage) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailMessage.getMessage(), "text/html; charset=UTF-8");
        return bodyPart;
    }

    private void processHeaders(EmailMessage emailMessage, SMTPMessage message) throws MessagingException {
        if (Items.isNotEmpty(emailMessage.getHeaders())) {
            for (Map.Entry<String, String> entry : emailMessage.getHeaders().entrySet()) {
                message.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private void processExtensions(EmailMessage emailMessage, SMTPMessage message) throws MessagingException, IOException {
        if (Items.isNotEmpty(emailMessage.getExtensions())) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : emailMessage.getExtensions().entrySet()) {
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

    public void send(EmailMessage message) throws IOException, MessagingException {
        Transport.send(createMessage(message));
    }
}
