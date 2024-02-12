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

package com.eslibs.common.email

import com.eslibs.common.Constant
import com.eslibs.common.configuration.credentials.Credentials
import com.eslibs.common.model.data.OutputData
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
        return Emails.sender(
                System.getProperty("test_email_server"),
                Credentials.builder()
                        .login(System.getProperty("test_email_login"))
                        .password(System.getProperty("test_email_password"))
                        .build()
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
        sender.send(Message.builder()
                .recipients("memphisprogramming@gmail.com")
                .subject("Spock Unit Test")
                .content("Spock Unit Test Body " + new Date())
                .build()
        )
    }

    @IgnoreIf({
        System.getProperty("test_email_server") == null || System.getProperty("test_email_login") == null || System.getProperty("test_email_password") == null
    })
    @Timeout(20)
    def "Send email with russian attachment name"() {
        when:
        def sender = createSender()
        def file = Files.createTempFile("test_mail_file", "txt")
        Files.write(file, "Пробное содержимое".bytes)
        def message = Message.builder()
                .recipients("memphisprogramming@gmail.com")
                .subject("Тайтл на русском")
                .content("Тестовка на русском " + new Date())
                .attachments([
                        Message.Attachment.builder()
                                .data(OutputData.create(
                                        "Тестовое имя файла (из файла).txt",
                                        file,
                                        file
                                )).build()
                ])
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
        def message = Message.builder()
                .recipients("memphisprogramming@gmail.com")
                .subject("Тайтл на русском")
                .content("Тестовка на русском " + new Date())
                .attachments([
                        Message.Attachment.builder()
                                .data(OutputData.create(
                                        'Тестовое имя файла (из байт).txt',
                                        'text/plain',
                                        Constant.bytes(fileContent),
                                )).build()
                ])
                .build()
        then:
        sender.send(message)
    }
}
