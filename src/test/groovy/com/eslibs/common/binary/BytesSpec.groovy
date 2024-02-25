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

package com.eslibs.common.binary

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.06.15
 */
class BytesSpec extends Specification {

    def "Correct format byte in hex (uppercase)"() {
        expect:
        Bytes.hex((byte) 0) == "00"
        Bytes.hex((byte) 1) == "01"
        Bytes.hex((byte) 15) == "0F"
        Bytes.hex((byte) 160) == "A0"
        Bytes.hex((byte) 255) == "FF"
    }

    def "Correct format bytes in hex (uppercase) with space split"() {
        expect:
        Bytes.hex([] as byte[]) == ""
        Bytes.hex([0, 0, 0, 0, 0] as byte[]) == "00 00 00 00 00"
        Bytes.hex([0, 1, 15, 160, 255] as byte[]) == "00 01 0F A0 FF"

        when:
        Bytes.hex(null)
        then:
        thrown(NullPointerException)
    }

    def "Convert to BCD int need return array for value>=0, width>0 else IllegalArgumentException"() {
        expect:
        Bytes.intToBCD(0, 1) == [0x00] as byte[]
        Bytes.intToBCD(1, 1) == [0x01] as byte[]
        Bytes.intToBCD(11, 1) == [0x11] as byte[]
        Bytes.intToBCD(99, 1) == [0x99] as byte[]
        Bytes.intToBCD(6813594, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        Bytes.intToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.intToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.intToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Convert to BCD long need return array for value>=0, width>0 else IllegalArgumentException"() {
        expect:
        Bytes.longToBCD(0, 1) == [0x00] as byte[]
        Bytes.longToBCD(1, 1) == [0x01] as byte[]
        Bytes.longToBCD(11, 1) == [0x11] as byte[]
        Bytes.longToBCD(99, 1) == [0x99] as byte[]
        Bytes.longToBCD(6813594, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        Bytes.longToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.longToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.longToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Convert to BCD double need return array for value>=0, width>0 else IllegalArgumentException"() {
        expect:
        Bytes.doubleToBCD(0, 1) == [0x00] as byte[]
        Bytes.doubleToBCD(1, 2) == [0x01, 0x00] as byte[]
        Bytes.doubleToBCD(11, 2) == [0x11, 0x00] as byte[]
        Bytes.doubleToBCD(99, 2) == [0x99, 0x00] as byte[]
        Bytes.doubleToBCD(68135.94, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        Bytes.doubleToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.doubleToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.doubleToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Convert to BCD double need return array for value>=0, width>0 and when width correct (value*decimalCount) else IllegalArgumentException"() {
        expect:
        Bytes.doubleToBCD(0, 5, 1) == [0x00] as byte[]
        Bytes.doubleToBCD(1, 5, 3) == [0x10, 0x00, 0x00] as byte[]
        Bytes.doubleToBCD(11, 5, 4) == [0x01, 0x10, 0x00, 0x00] as byte[]
        Bytes.doubleToBCD(99, 5, 5) == [0x00, 0x09, 0x90, 0x00, 0x00] as byte[]
        Bytes.doubleToBCD(68135.94, 3, 5) == [0x00, 0x68, 0x13, 0x59, 0x40] as byte[]

        when:
        Bytes.doubleToBCD(-1, 5, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.doubleToBCD(5, 5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        Bytes.doubleToBCD(55534, 5, 2)
        then:
        thrown(IllegalArgumentException)
    }

    def "int16 to byte[] must be correct"() {
        expect:
        Bytes.getUnsignedShort(Bytes.getInt16Bytes(value), 0) == value
        Bytes.getUnsignedShortLE(Bytes.getInt16BytesLE(value), 0) == value
        where:
        value << [0x0000, 0x0001, 0x00FF, 0xFF00, 0xFFFF]
    }

    def "int32 to byte[] must be correct"() {
        expect:
        Bytes.getInt(Bytes.getInt32Bytes(value), 0) == value
        Bytes.getIntLE(Bytes.getInt32BytesLE(value), 0) == value
        where:
        value << [0x00000000, 0x00000001, 0x000000FF, 0x0000FF00, 0x0000FFFF, 0x00FFFFFF, -0x1]
    }

    def "long to byte[] must be correct"() {
        expect:
        Bytes.getLong(Bytes.getLongBytes(value), 0) == value
        Bytes.getLongLE(Bytes.getLongBytesLE(value), 0) == value
        where:
        value << [0x00000000L, 0x00000001L, 0x000000FFL, 0x0000FF00L, 0x0000FFFFL, 0x00FFFFFFL, -0x1L]
    }

}
