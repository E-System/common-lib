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

package com.eslibs.common.security

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.10.16
 */
class HashSpec extends Specification {

    def "BCrypt"() {
        expect:
        Hash.bcrypt().get("Hello").startsWith('$2a$12$')
    }

    def "CRC32"() {
        expect:
        Hash.crc32().get("Hello") == 4157704578
    }

    def "CRC16"() {
        expect:
        Hash.crc16().get("Hello") == 56026
    }

    def "SHA-1"() {
        expect:
        Hash.sha1().get("Hello") == 'f7ff9e8b7bb2e09b70935a5d785e0cc5d9d0abf0'
    }

    def "hmacSha256"() {
        expect:
        Hash.hmacSha256("secret_key").get("Test content") == "oKL8Wjg+l73byfPL1FIYcV2T1IcpAHhopwScTcOoSIU="
    }

    def "crc16ccitt required not null value"() {
        when:
        Hash.crc16ccitt().get(null as byte[])
        Hash.crc16ccitt().get(null as String)
        then:
        thrown NullPointerException
    }

    def "crc16ccitt must be correct"() {
        expect:
        Hash.crc16ccitt().get(src) == result
        where:
        src                       || result
        '1234567890'.bytes        || 0x3218
        'qwertyu'.bytes           || 0x4DBE
        '1234567890qwertyu'.bytes || 0x92D8
    }

    def "crc16ccitt with skip must be correct without IndexOutException "() {
        expect:
        Hash.crc16ccitt(skipIdx, skipLen).get(src) == Hash.crc16ccitt().get(target)
        where:
        src                       || skipIdx || skipLen || target
        '1234567890'.bytes        || 0       || 2       || '34567890'.bytes
        'qwertyu'.bytes           || 2       || 3       || 'qwyu'.bytes
        '1234567890qwertyu'.bytes || 10      || 1       || '1234567890wertyu'.bytes
        '1234567890'.bytes        || 0       || 0       || '1234567890'.bytes
        '1234567890'.bytes        || 100     || 200     || '1234567890'.bytes
        '1234567890'.bytes        || -100    || -200    || '1234567890'.bytes
    }
}

