/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2019
 */

package com.es.lib.common.message


import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 22.01.2019
 */
class MessageBundleSpec extends Specification {

    def "MessageBundle values loaded by path should be correct"() {
        expect:
        MessageBundle bundle = new MessageBundle("com/es/lib/common/message/codes.properties")
        Objects.equals(value, bundle.get(code))
        where:
        value               | code
        "Value 255"         | (byte) -1
        "Value 1"           | (byte) 1
        "Value 255"         | (byte) 255
        "Value -1"          | -1
        "Value 1"           | 1
        "Value 255"         | 255
        "Value 256"         | 256
        "Value string_code" | 'string_code'
        "Value null"        | null
        null                | 'none'
    }

    def "MessageBundle values loaded by class should be correct"() {
        expect:
        MessageBundle bundle = new MessageBundle(MessageBundle.class, "codes")
        Objects.equals(value, bundle.get(code))
        where:
        value               | code
        "Value 255"         | (byte) -1
        "Value 1"           | (byte) 1
        "Value 255"         | (byte) 255
        "Value -1"          | -1
        "Value 1"           | 1
        "Value 255"         | 255
        "Value 256"         | 256
        "Value string_code" | 'string_code'
        "Value null"        | null
        null                | 'none'
    }

    def "MessageBundle default values should be correct"() {
        expect:
        MessageBundle bundle = new MessageBundle(MessageBundle.class, "codes")
        Objects.equals(value, bundle.get(code, defaultValue))
        where:
        value               | code          | defaultValue
        "Value 255"         | (byte) -1     | null
        "Value 1"           | (byte) 1      | null
        "Value 255"         | (byte) 255    | null
        "Value -1"          | -1            | null
        "Value 1"           | 1             | null
        "Value 255"         | 255           | null
        "Value 256"         | 256           | null
        "Value string_code" | 'string_code' | null
        "Value null"        | null          | null
        null                | 'none'        | null
        "Value default"     | (byte) 100    | "Value default"
        "Value default"     | 100           | "Value default"
        "Value default"     | 'none'        | "Value default"
    }

    def "MessageBundle default values with locale, should be correct"() {
        expect:
        MessageBundle bundle = new MessageBundle(MessageBundle.class, "localized")
        Objects.equals(value, bundle.getLocalized(code, defaultValue, locale))
        where:
        value                     | code          | defaultValue    | locale
        "Value 255"               | (byte) -1     | null            | "ru_RU"
        "Value 1"                 | (byte) 1      | null            | "ru_RU"
        "Value 255"               | (byte) 255    | null            | "ru_RU"
        "Value -1"                | -1            | null            | "ru_RU"
        "Value 1"                 | 1             | null            | "ru_RU"
        "Value 255"               | 255           | null            | "ru_RU"
        "Value 256"               | 256           | null            | "ru_RU"
        "Value string_code"       | 'string_code' | null            | "ru_RU"
        "Value null"              | null          | null            | "ru_RU"
        null                      | 'none'        | null            | "ru_RU"
        "Value default"           | (byte) 100    | "Value default" | "ru_RU"
        "Value default"           | 100           | "Value default" | "ru_RU"
        "Value default"           | 'none'        | "Value default" | "ru_RU"

        "Value 255 en_US"         | (byte) -1     | null            | "en_US"
        "Value 1 en_US"           | (byte) 1      | null            | "en_US"
        "Value 255 en_US"         | (byte) 255    | null            | "en_US"
        "Value -1 en_US"          | -1            | null            | "en_US"
        "Value 1 en_US"           | 1             | null            | "en_US"
        "Value 255 en_US"         | 255           | null            | "en_US"
        "Value 256 en_US"         | 256           | null            | "en_US"
        "Value string_code en_US" | 'string_code' | null            | "en_US"
        "Value null en_US"        | null          | null            | "en_US"
        null                      | 'none'        | null            | "en_US"
        "Value default"           | (byte) 100    | "Value default" | "en_US"
        "Value default"           | 100           | "Value default" | "en_US"
        "Value default"           | 'none'        | "Value default" | "en_US"
    }

}
