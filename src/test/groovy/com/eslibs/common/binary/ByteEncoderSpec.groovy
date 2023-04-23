package com.eslibs.common.binary

import spock.lang.Specification

class ByteEncoderSpec extends Specification {

    def "Encode with null data"() {
        when:
        def encoder = new ByteEncoder(null)
        then:
        encoder.encode() == null
        encoder.urlEncode() == null
        encoder.mimeEncode() == null
        encoder.hexEncode() == null
        encoder.hexEncode(true) == null
        encoder.string() == null
    }

    def "Encode with data"() {
        when:
        def encoder = new ByteEncoder('null'.getBytes())
        then:
        encoder.encode() == 'bnVsbA=='
        encoder.urlEncode() == 'bnVsbA=='
        encoder.mimeEncode() == 'bnVsbA=='
        encoder.hexEncode() == '6e756c6c'
        encoder.hexEncode(true) == '6E756C6C'
        encoder.string() == 'null'
    }

    def "Encode with data with space"() {
        when:
        def encoder = new ByteEncoder('null null'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxs'
        encoder.urlEncode() == 'bnVsbCBudWxs'
        encoder.mimeEncode() == 'bnVsbCBudWxs'
        encoder.hexEncode() == '6e756c6c206e756c6c'
        encoder.hexEncode(true) == '6E756C6C206E756C6C'
        encoder.string() == 'null null'
    }

    def "Encode with data with russian"() {
        when:
        def encoder = new ByteEncoder('null null Привет'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.urlEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.mimeEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.hexEncode() == '6e756c6c206e756c6c20d09fd180d0b8d0b2d0b5d182'
        encoder.hexEncode(true) == '6E756C6C206E756C6C20D09FD180D0B8D0B2D0B5D182'
        encoder.string() == 'null null Привет'
    }

    def "Encode with data with url"() {
        when:
        def encoder = new ByteEncoder('null null Привет'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.urlEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.mimeEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.hexEncode() == '6e756c6c206e756c6c20d09fd180d0b8d0b2d0b5d182'
        encoder.hexEncode(true) == '6E756C6C206E756C6C20D09FD180D0B8D0B2D0B5D182'
        encoder.string() == 'null null Привет'
    }

    def "Encode with data with long text"() {
        when:
        def encoder = new ByteEncoder('Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.'.getBytes())
        then:
        encoder.encode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
        encoder.urlEncode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
        encoder.mimeEncode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz\r\nIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg\r\ndGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu\r\ndWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo\r\nZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
        encoder.hexEncode() == '4d616e2069732064697374696e677569736865642c206e6f74206f6e6c792062792068697320726561736f6e2c2062757420627920746869732073696e67756c61722070617373696f6e2066726f6d206f7468657220616e696d616c732c2077686963682069732061206c757374206f6620746865206d696e642c20746861742062792061207065727365766572616e6365206f662064656c6967687420696e2074686520636f6e74696e75656420616e6420696e6465666174696761626c652067656e65726174696f6e206f66206b6e6f776c656467652c2065786365656473207468652073686f727420766568656d656e6365206f6620616e79206361726e616c20706c6561737572652e'
        encoder.hexEncode(true) == '4D616E2069732064697374696E677569736865642C206E6F74206F6E6C792062792068697320726561736F6E2C2062757420627920746869732073696E67756C61722070617373696F6E2066726F6D206F7468657220616E696D616C732C2077686963682069732061206C757374206F6620746865206D696E642C20746861742062792061207065727365766572616E6365206F662064656C6967687420696E2074686520636F6E74696E75656420616E6420696E6465666174696761626C652067656E65726174696F6E206F66206B6E6F776C656467652C2065786365656473207468652073686F727420766568656D656E6365206F6620616E79206361726E616C20706C6561737572652E'
        encoder.string() == 'Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.'
    }
}
