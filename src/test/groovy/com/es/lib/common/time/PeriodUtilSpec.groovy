package com.es.lib.common.time

import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 12.05.2018
 */
class PeriodUtilSpec extends Specification {

    def "one year"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1)
        def result = PeriodUtil.pretty(date, nextDate, false)
        then:
        result == '1 год'
    }

    def "one year and one day"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1)
        def result = PeriodUtil.pretty(date, nextDate, false)
        then:
        result == '1 год 1 дн.'
    }

    def "one year and one day and two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1).plusHours(2)
        def result = PeriodUtil.pretty(date, nextDate, false)
        then:
        result == '1 год 1 дн. 2 ч.'
    }

    def "two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2)
        def result = PeriodUtil.pretty(date, nextDate, false)
        then:
        result == '2 ч.'
    }

    def "two hours with minutes and seconds"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = PeriodUtil.pretty(date, nextDate, false)
        then:
        result == '2 ч. 11 м. 34 c.'
    }

    def "two hours with minutes and seconds (use braces)"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = PeriodUtil.pretty(date, nextDate, true)
        then:
        result == '(2 ч. 11 м. 34 c.)'
    }

}
