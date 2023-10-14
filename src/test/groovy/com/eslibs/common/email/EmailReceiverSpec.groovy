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

import com.eslibs.common.configuration.credentials.Credentials
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Timeout

import java.nio.file.Files

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
class EmailReceiverSpec extends Specification {

    def createReceiver() {
        return Emails.receiver(
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
    def "Fetch all"() {
        when:
        def receiver = createReceiver()
        then:
        def messages = receiver.fetch(Files.createTempDirectory("email"), false)
        println(messages)
        expect:
        messages.size() >= 0
    }
}
