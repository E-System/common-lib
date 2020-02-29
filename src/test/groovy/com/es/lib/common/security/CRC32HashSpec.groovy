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

package com.es.lib.common.security

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.10.16
 */
class CRC32HashSpec extends Specification {

    def "crc16ccitt required not null value"() {
        when:
        CRCHash.crc16ccitt().get(null as byte[])
        CRCHash.crc16ccitt().get(null as String)
        then:
        thrown NullPointerException
    }

    def "crc16ccitt must be correct"() {
        expect:
        CRCHash.crc16ccitt().get(src) == result
        where:
        src                       || result
        '1234567890'.bytes        || 0x3218
        'qwertyu'.bytes           || 0x4DBE
        '1234567890qwertyu'.bytes || 0x92D8
    }

    def "crc16ccitt with skip must be correct without IndexOutException "() {
        expect:
        CRCHash.crc16ccitt(skipIdx, skipLen).get(src) == CRCHash.crc16ccitt().get(target)
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

