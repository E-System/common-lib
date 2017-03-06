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

import com.es.lib.common.collection.CollectionUtil;
import com.es.lib.common.email.common.BaseEmailProcessor;
import com.es.lib.common.email.config.SMTPServerConfiguration;
import com.sun.mail.smtp.SMTPMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static javax.mail.Part.ATTACHMENT;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class EmailSender extends BaseEmailProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    public EmailSender(SMTPServerConfiguration configuration) throws IOException {
        super(configuration);
    }

    private Message createMessage(EmailMessage emailMessage) throws MessagingException, IOException {
        SMTPMessage smtpMessage = createSMTPMessage(emailMessage);

        String from = StringUtils.isEmpty(emailMessage.getFrom()) ? getLogin() : emailMessage.getFrom();
        String backAddress = StringUtils.isEmpty(emailMessage.getBackAddress()) ? getLogin() : emailMessage.getBackAddress();
        LOG.trace("From: {}; Back Address: {}", from, backAddress);
        smtpMessage.setFrom(new InternetAddress(backAddress, from));
        if (StringUtils.isNotEmpty(emailMessage.getBackAddress())) {
            smtpMessage.setReplyTo(
                InternetAddress.parse(
                    emailMessage.getBackAddress()
                )
            );
        }

        InternetAddress sender;
        try {
            sender = new InternetAddress(from);
        } catch (AddressException e) {
            sender = new InternetAddress(getLogin());
        }
        LOG.trace("Sender: {}", sender);
        smtpMessage.setEnvelopeFrom(sender.getAddress());
        smtpMessage.setSender(sender);

        smtpMessage.setRecipients(Message.RecipientType.TO, emailMessage.getDestinations());
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
            if (attachment.getContent().isByteArray()) {
                EmailByteArrayContent content = (EmailByteArrayContent) attachment.getContent();
                message.setContent(
                    new ByteArrayDataSource(content.getBytes(), content.getType()),
                    content.getType()
                );
            } else {
                EmailFileContent content = (EmailFileContent) attachment.getContent();

                FileDataSource fds = new FileDataSource(new File(content.getTarget()));
                message.setDataHandler(new DataHandler(fds));
                message.setFileName(StringUtils.isNotEmpty(content.getName()) ? content.getName() : fds.getName());
                message.setDisposition(ATTACHMENT);
            }
        } else if (CollectionUtil.isEmpty(emailMessage.getAttachments())) {
            message.setContent(emailMessage.getMessage(), "text/html; charset=UTF-8");
        } else if (StringUtils.isNotEmpty(emailMessage.getMessage())) {
            Multipart multipart = new MimeMultipart("related");

            multipart.addBodyPart(createMessageBodyPart(emailMessage));

            processAttachments(emailMessage, multipart);

            message.setContent(multipart);
        }
    }

    private void processAttachments(EmailMessage emailMessage, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
        for (EmailAttachment attachment : emailMessage.getAttachments()) {
            BodyPart mimeBodyPart = new MimeBodyPart();

            DataSource dataSource;
            if (attachment.getContent().isByteArray()) {
                EmailByteArrayContent content = (EmailByteArrayContent) attachment.getContent();
                dataSource = new ByteArrayDataSource(content.getBytes(), content.getType());
                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                if (StringUtils.isNotBlank(content.getName())) {
                    mimeBodyPart.setFileName(content.getName());
                }
                mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");
                mimeBodyPart.setHeader("Content-type", content.getType() + "; charset=utf-8");
            } else {
                EmailFileContent content = (EmailFileContent) attachment.getContent();
                dataSource = new FileDataSource(content.getTarget());
                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                mimeBodyPart.setFileName(content.getName());
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
        if (CollectionUtil.isNotEmpty(emailMessage.getHeaders())) {
            for (Map.Entry<String, String> entry : emailMessage.getHeaders().entrySet()) {
                message.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private void processExtensions(EmailMessage emailMessage, SMTPMessage message) throws MessagingException, IOException {
        if (CollectionUtil.isNotEmpty(emailMessage.getExtensions())) {
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
                        LOG.trace("Find SIZE extension. Calculate: {}", value);
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
