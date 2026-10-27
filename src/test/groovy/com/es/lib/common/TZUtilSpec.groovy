package com.es.lib.common

import spock.lang.Specification

import java.util.TimeZone

class TZUtilSpec extends Specification {

    def "Check isCorrect"() {
        expect:
        TZUtil.isCorrect("Europe/Moscow")
        TZUtil.isCorrect("UTC")
        !TZUtil.isCorrect("Invalid/Zone_Name")
    }

    def "Get available timezones"() {
        when:
        Collection<TimeZone> available = TZUtil.getAvailable()

        then:
        available != null
        !available.isEmpty()
        available.every { tz ->
            tz.ID.matches("^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*")
        }
    }
}
