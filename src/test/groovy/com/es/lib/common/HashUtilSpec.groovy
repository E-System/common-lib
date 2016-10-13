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

package com.es.lib.common

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.10.16
 */
class HashUtilSpec extends Specification {

    def "CRC16CCITT required not null array"() {
        when:
        HashUtil.CRC16CCITT(null)
        then:
        thrown NullPointerException
    }

    def "CRC16CCITT must be correct"() {
        expect:
        HashUtil.CRC16CCITT(src) == result
        where:
        src                       || result
        '1234567890'.bytes        || 0x3218
        'qwertyu'.bytes           || 0x4DBE
        '1234567890qwertyu'.bytes || 0x92D8
    }

    def "CRC16CCITT with skip must be correct without IndexOutException "() {
        expect:
        HashUtil.CRC16CCITT(src, skipIdx, skipLen) == HashUtil.CRC16CCITT(target)
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

