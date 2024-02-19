package com.eslibs.common.binary.tlv

import spock.lang.Specification

class TlvSpec extends Specification {

    def "VLN"() {
        expect:
        Tlv.from(1, 0, true).bytes == new byte[]{0x1, 0x0, 0x1, 0x0, 0x0}
        Tlv.from(1, 320000, true).bytes == new byte[]{0x1, 0x0, 0x3, 0x0, 0x0, 0xE2, 0x04}
    }

    def "STLV"() {
        expect:
        Tlv.from(1,
                Tlv.from(1, 0, true),
                Tlv.from(1, 320000, true)
        ).bytes == new byte[]{0x1, 0x0, 0xC, 0x0, 0x1, 0x0, 0x1, 0x0, 0x0, 0x1, 0x0, 0x3, 0x0, 0x0, 0xE2, 0x04}
    }
}
