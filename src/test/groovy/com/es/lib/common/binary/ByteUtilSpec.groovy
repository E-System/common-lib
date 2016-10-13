/*
 * Copyright (c) Extended System - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Extended System team (https://ext-system.com), 2016
 */

package com.es.lib.common.binary

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.06.15
 */
class ByteUtilSpec extends Specification {

    def "Должен быть корректно отформатирован байт в hex виде(верхний регистр)"() {
        expect:
        ByteUtil.toHex((byte) 0) == "00"
        ByteUtil.toHex((byte) 1) == "01"
        ByteUtil.toHex((byte) 15) == "0F"
        ByteUtil.toHex((byte) 160) == "A0"
        ByteUtil.toHex((byte) 255) == "FF"
    }

    def "Должен быть корректно отформатирован массив байтов в hex виде(верхний регистр) с разделением пробелом"() {
        expect:
        ByteUtil.toHex([] as byte[]) == ""
        ByteUtil.toHex([0, 0, 0, 0, 0] as byte[]) == "00 00 00 00 00"
        ByteUtil.toHex([0, 1, 15, 160, 255] as byte[]) == "00 01 0F A0 FF"

        when:
        ByteUtil.toHex(null)
        then:
        thrown(NullPointerException)
    }

    def "Преобразование в BCD целого числа должно возвращать массив для value>=0, width>0 и когда width достаточно иначе IllegalArgumentException"() {
        expect:
        ByteUtil.intToBCD(0, 1) == [0x00] as byte[]
        ByteUtil.intToBCD(1, 1) == [0x01] as byte[]
        ByteUtil.intToBCD(11, 1) == [0x11] as byte[]
        ByteUtil.intToBCD(99, 1) == [0x99] as byte[]
        ByteUtil.intToBCD(6813594, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        ByteUtil.intToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.intToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.intToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Преобразование в BCD целого long числа должно возвращать массив для value>=0, width>0 и когда width достаточно иначе IllegalArgumentException"() {
        expect:
        ByteUtil.longToBCD(0, 1) == [0x00] as byte[]
        ByteUtil.longToBCD(1, 1) == [0x01] as byte[]
        ByteUtil.longToBCD(11, 1) == [0x11] as byte[]
        ByteUtil.longToBCD(99, 1) == [0x99] as byte[]
        ByteUtil.longToBCD(6813594, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        ByteUtil.longToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.longToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.longToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Преобразование в BCD дробного числа должно возвращать массив для value>=0, width>0 и когда width достаточно(value*100) иначе IllegalArgumentException"() {
        expect:
        ByteUtil.doubleToBCD(0, 1) == [0x00] as byte[]
        ByteUtil.doubleToBCD(1, 2) == [0x01, 0x00] as byte[]
        ByteUtil.doubleToBCD(11, 2) == [0x11, 0x00] as byte[]
        ByteUtil.doubleToBCD(99, 2) == [0x99, 0x00] as byte[]
        ByteUtil.doubleToBCD(68135.94, 5) == [0x00, 0x06, 0x81, 0x35, 0x94] as byte[]

        when:
        ByteUtil.doubleToBCD(-1, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.doubleToBCD(5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.doubleToBCD(55534, 2)
        then:
        thrown(IllegalArgumentException)

    }

    def "Преобразование в BCD дробного числа должно возвращать массив для value>=0, width>0 и когда width достаточно(value*decimalCount) иначе IllegalArgumentException"() {
        expect:
        ByteUtil.doubleToBCD(0, 5, 1) == [0x00] as byte[]
        ByteUtil.doubleToBCD(1, 5, 3) == [0x10, 0x00, 0x00] as byte[]
        ByteUtil.doubleToBCD(11, 5, 4) == [0x01, 0x10, 0x00, 0x00] as byte[]
        ByteUtil.doubleToBCD(99, 5, 5) == [0x00, 0x09, 0x90, 0x00, 0x00] as byte[]
        ByteUtil.doubleToBCD(68135.94, 3, 5) == [0x00, 0x68, 0x13, 0x59, 0x40] as byte[]

        when:
        ByteUtil.doubleToBCD(-1, 5, 5)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.doubleToBCD(5, 5, 0)
        then:
        thrown(IllegalArgumentException)

        when:
        ByteUtil.doubleToBCD(55534, 5, 2)
        then:
        thrown(IllegalArgumentException)
    }
}
