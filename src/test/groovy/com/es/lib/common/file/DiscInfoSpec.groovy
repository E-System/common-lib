package com.es.lib.common.file

import spock.lang.Specification

class DiscInfoSpec extends Specification {

    def "Create"() {
        expect:
        with(DiscInfo.create()) {
            it.total != 0
            it.free != 0
        }
    }

    def "Create from strings and asString formatting"() {
        given:
        long totalBytes = 10L * 1024 * 1024 * 1024
        long freeBytes = 5L * 1024 * 1024 * 1024

        when:
        DiscInfo info = DiscInfo.create(String.valueOf(totalBytes), String.valueOf(freeBytes))

        then:
        info != null
        info.total == totalBytes
        info.free == freeBytes
        info.asString().contains("5")
        info.asString().contains("10")
        info.toString().contains("DiscInfo")

        expect:
        DiscInfo.create("", "100") == null
        DiscInfo.create("100", null) == null
    }
}
