package com.es.lib.common.time

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 20.12.2017
 */
class TimeConverterSpec extends Specification {

    def "AsString"() {
        expect:
        TimeConverter.asString(value) == result
        where:
        value   || result
        0       || "00:00"
        1       || "00:00"
        1000    || "00:00:01"
        10000   || "00:00:10"
        60000   || "00:01"
        61000   || "00:01:01"
        3600000 || "01:00"
    }

    def "AsLong"() {
        expect:
        TimeConverter.asLong(value) == result
        where:
        value      || result
        null       || null
        ""         || null
        "0"        || 0
        "1"        || 3600000
        "1:1"      || 3660000
        "1:1:2"    || 3662000
        "01:01:02" || 3662000
    }
}