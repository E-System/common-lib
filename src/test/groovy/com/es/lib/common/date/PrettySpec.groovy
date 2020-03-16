package com.es.lib.common.date

import com.es.lib.common.text.Texts
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month
import java.util.function.BiFunction

class PrettySpec extends Specification {

    @Shared
    def LOCALIZATION = new BiFunction<PrettyInterval.DurationType, Long, String>() {
        @Override
        String apply(PrettyInterval.DurationType type, Long value) {
            switch (type) {
                case PrettyInterval.DurationType.YEAR:
                    return value + " " + Texts.pluralize(value, "year", "years", "years")
                case PrettyInterval.DurationType.MONTH:
                    return value + " mon."
                case PrettyInterval.DurationType.DAY:
                    return value + " d."
                case PrettyInterval.DurationType.HOUR:
                    return value + " h."
                case PrettyInterval.DurationType.MINUTE:
                    return value + " m."
                case PrettyInterval.DurationType.SECOND:
                    return value + " s."
                default:
                    return "";
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
