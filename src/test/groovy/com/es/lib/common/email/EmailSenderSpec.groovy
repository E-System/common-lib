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

package com.es.lib.common.email


import com.es.lib.common.email.config.SMTPServerConfiguration
import com.es.lib.common.security.model.Credentials
import com.es.lib.common.model.data.OutputData
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Timeout

import java.nio.file.Files

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.06.15
 */
class EmailSenderSpec extends Specification {

    def createSender() {
        return new EmailSender(
            new SMTPServerConfiguration(
                SMTPServerConfiguration.PRESETS.get(System.getProperty("test_email_server")),
                new Credentials(
                    System.getProperty("test_email_login"),
                    System.getProperty("test_email_password")
                )
            )
        )
    }

    def createSmtpsSender() {
        return new EmailSender(
            new SMTPServerConfiguration(
                SMTPServerConfiguration.PRESETS.get(System.getProperty("test_email_server") + "_tls"),
                new Credentials(
                    System.getProperty("test_email_login"),
                    System.getProperty("test_email_password")
                )
            )
        )
    }

    @IgnoreIf({
        System.getProperty("test_email_server") == null || System.getProperty("test_email_login") == null || System.getProperty("test_email_password") == null
    })
    @Timeout(20)
    def "Send email"() {
        when:
        def sender = createSender()
        then:
        sender.send(EmailMessage.builder("memphisprogramming@gmail.com", "Spock Unit Test", "Spock Unit Test Body " + new Date()).build())
    }

    @IgnoreIf({
        System.getProperty("test_email_server") == null || System.getProperty("test_email_login") == null || System.getProperty("test_email_password") == null
    })
    @Timeout(20)
    def "Send email (TLS)"() {
        when:
        def sender = createSmtpsSender()
        then:
        sender.send(EmailMessage.builder("memphisprogramming@gmail.com", "Spock Unit Test", "Spock Unit Test Body (TLS) " + new Date()).build())
    }

    @IgnoreIf({
        System.getProperty("test_email_server") == null || System.getProperty("test_email_login") == null || System.getProperty("test_email_password") == null
    })
    @Timeout(20)
    def "Send email with russian attachment name"() {
        when:
        def sender = createSender()
        def file = Files.createTempFile("тестовый файл", "txt")
        file.write('Пробное содержимое')
        def attachment = new EmailAttachment(
            OutputData.create(
                "Тестовое имя файла (из файла).txt",
                file.toString(),
                file
            )
        )
        def message = EmailMessage
            .builder("memphisprogramming@gmail.com", "Тайтл на русском", "Тестовка на русском " + new Date())
            .attachment(attachment)
            .build()
        then:
        sender.send(message)
    }

    @IgnoreIf({
        System.getProperty("test_email_server") == null || System.getProperty("test_email_login") == null || System.getProperty("test_email_password") == null
    })
    @Timeout(20)
    def "Send email with russian attachment name (from bytes)"() {
        when:
        def sender = createSender()
        def fileContent = 'Пробное содержимое'
        def attachment = new EmailAttachment(
            OutputData.create(
                'Тестовое имя файла (из байт).txt',
                'text/plain',
                fileContent.getBytes(),
            )
        )
        def message = EmailMessage
            .builder("memphisprogramming@gmail.com", "Тайтл на русском", "Тестовка на русском " + new Date())
            .attachment(attachment)
            .build()
        then:
        sender.send(message)
    }
}
