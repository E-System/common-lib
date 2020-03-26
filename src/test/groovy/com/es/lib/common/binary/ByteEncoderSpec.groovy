package com.es.lib.common.binary

import spock.lang.Specification

class ByteEncoderSpec extends Specification {

    def "Encode with null data"() {
        when:
        def encoder = new ByteEncoder(null)
        then:
        encoder.encode() == null
        encoder.urlEncode() == null
        encoder.mimeEncode() == null
    }

    def "Encode with data"() {
        when:
        def encoder = new ByteEncoder('null'.getBytes())
        then:
        encoder.encode() == 'bnVsbA=='
        encoder.urlEncode() == 'bnVsbA=='
        encoder.mimeEncode() == 'bnVsbA=='
    }

    def "Encode with data with space"() {
        when:
        def encoder = new ByteEncoder('null null'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxs'
        encoder.urlEncode() == 'bnVsbCBudWxs'
        encoder.mimeEncode() == 'bnVsbCBudWxs'
    }

    def "Encode with data with russian"() {
        when:
        def encoder = new ByteEncoder('null null Привет'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.urlEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.mimeEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
    }

    def "Encode with data with url"() {
        when:
        def encoder = new ByteEncoder('null null Привет'.getBytes())
        then:
        encoder.encode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.urlEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
        encoder.mimeEncode() == 'bnVsbCBudWxsINCf0YDQuNCy0LXRgg=='
    }

    def "Encode with data with long text"() {
        when:
        def encoder = new ByteEncoder('Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.'.getBytes())
        then:
        encoder.encode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
        encoder.urlEncode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
        encoder.mimeEncode() == 'TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz\r\nIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg\r\ndGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu\r\ndWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo\r\nZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4='
    }
}
