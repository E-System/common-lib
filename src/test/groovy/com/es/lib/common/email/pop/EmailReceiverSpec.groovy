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

package com.es.lib.common.email.pop

import com.es.lib.common.email.config.EmailAuth
import com.es.lib.common.email.config.POP3ServerConfiguration
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.05.16
 */
class EmailReceiverSpec extends Specification {

    def "GetAll"() {
        when:
        def receiver = new EmailReceiver(
                new POP3ServerConfiguration(
                        POP3ServerConfiguration.PRESETS.get(System.getProperty("test_email_server")),
                        new EmailAuth(
                                System.getProperty("test_email_login"),
                                System.getProperty("test_email_password")
                        )
                )
        )
        then:
        def messages = receiver.getAll("/tmp", false)
        expect:
        messages.size() >= 0
    }
}
