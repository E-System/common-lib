package com.eslibs.common.date

import com.eslibs.common.text.Texts
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.function.BiFunction

class PrettySpec extends Specification {

    @Shared
    def LOCALIZATION = new BiFunction<ChronoUnit, Long, String>() {

        @Override
        String apply(ChronoUnit type, Long value) {
            switch (type) {
                case ChronoUnit.YEARS:
                    return value + " " + Texts.pluralize(value, "year", "years", "years")
                case ChronoUnit.MONTHS:
                    return value + " mon."
                case ChronoUnit.DAYS:
                    return value + " d."
                case ChronoUnit.HOURS:
                    return value + " h."
                case ChronoUnit.MINUTES:
                    return value + " m."
                case ChronoUnit.SECONDS:
                    return value + " s."
                default:
                    return ""
            }
        }
    }

    def "One year"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == (date.getDayOfMonth() == 29 && date.getMonth() == Month.FEBRUARY ? '11 мес. 30 дн.' : '1 год')
    }

    def "One year and one day"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == '1 год 1 дн.'
    }

    def "One year and one day (localization)"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1)
        def result = Dates.pretty(false, LOCALIZATION).get(date, nextDate)
        then:
        result == '1 year 1 d.'
    }

    def "One year and one day and two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1).plusHours(2)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == '1 год 1 дн. 2 ч.'
    }

    def "One year and one day and two hours before"() {
        when:
        def date = LocalDateTime.of(2022, 3, 24, 20, 1, 2)
        def nextDate = date.plusYears(1).plusDays(1).plusHours(2)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == '1 год 1 дн. 2 ч.'
    }

    def "Two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == '2 ч.'
    }

    def "Two hours with minutes and seconds"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = Dates.pretty(false).get(date, nextDate)
        then:
        result == '2 ч. 11 м. 34 c.'
    }

    def "Two hours with minutes and seconds (use braces)"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = Dates.pretty(true).get(date, nextDate)
        then:
        result == '(2 ч. 11 м. 34 c.)'
    }
}
