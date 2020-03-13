/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.date

import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 06.02.15
 */
class DateUtilSpec extends Specification {

    @Shared
    int currentYear = LocalDateTime.now().year
    @Shared
    SimpleDateFormat sdf
    @Shared
    DateTimeFormatter dtf

    def setupSpec() {
        TimeZone.setDefault(TimeZone.getTimeZone('Asia/Krasnoyarsk'))
        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    }

    def "isZoneValid"() {
        expect:
        DateUtil.isZoneValid(id) == result
        where:
        id                 || result
        'Asia/Krasnoyarsk' || true
        'Europe/Moscow'    || true
        'Europe/M'         || false
    }

    def "availableZones"() {
        when:
        def zones = DateUtil.availableZones()
        println(zones)
        then:
        zones.contains(ZoneId.of('Asia/Krasnoyarsk'))
        zones.contains(ZoneId.of('Europe/Moscow'))
        !zones.contains(ZoneId.of('SystemV/PST8PDT'))
        !zones.contains(ZoneId.of('Zulu'))
        !zones.contains(ZoneId.of('UTC'))
    }

    def "NextDay"() {
        expect:
        DateUtil.generator(zone).nextDay(date) == result
        where:
        date                             | zone                       | result
        sdf.parse("06.02.2015 23:30:00") | ZoneId.of("Europe/Moscow") | sdf.parse("07.02.2015 23:30:00")
    }

    def "Contains"() {
        expect:
        DateUtil.contains(dbegin, dend, date, timeZone)
        where:
        dbegin                           | dend                             | date       | timeZone
        sdf.parse("06.02.2015 00:00:00") | sdf.parse("07.02.3015 00:00:00") | new Date() | ZoneId.of("Europe/Moscow")
    }

    def "IsAfterNow"() {
        expect:
        DateUtil.isAfterNow(date, zoneId) == result
        where:
        date                                                  | zoneId                     | result
        new Date()                                            | ZoneId.systemDefault()     | false
        sdf.parse("06.02." + (currentYear + 1) + " 23:00:00") | ZoneId.systemDefault()     | true
        sdf.parse("05.02." + (currentYear - 1) + " 23:00:00") | ZoneId.systemDefault()     | false
        new Date()                                            | ZoneId.of("Europe/Moscow") | false
        sdf.parse("06.02." + (currentYear + 1) + " 23:00:00") | ZoneId.of("Europe/Moscow") | true
        sdf.parse("05.02." + (currentYear - 1) + " 23:00:00") | ZoneId.of("Europe/Moscow") | false
    }

    def "IsBeforeToday"() {
        expect:
        DateUtil.isBeforeToday(date) == result
        where:
        date                                                                 | result
        LocalDateTime.parse("01.01." + (currentYear - 1) + " 23:00:00", dtf) | true
        LocalDateTime.parse("06.02." + (currentYear + 1) + " 23:00:00", dtf) | false
        LocalDateTime.now()                                                  | false
    }

    def "Получение даты в начале месяца"() {
        expect:
        DateUtil.generator().monthStart() == result
        where:
        result << Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    def "Получение даты в начале месяца (в Москве)"() {
        expect:
        DateUtil.generator().monthStart() == result
        where:
        zone                       | result
        ZoneId.of("Europe/Moscow") | Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    def "Количество дней между датами"() {
        expect:
        DateUtil.between(ChronoUnit.DAYS, start, end) == result
        where:
        start                            | end                              | result
        new Date()                       | new Date()                       | 0
        sdf.parse("05.02.2015 00:00:00") | sdf.parse("06.02.2015 00:00:00") | 1
        sdf.parse("05.02.2015 00:00:00") | sdf.parse("04.02.2015 00:00:00") | -1
    }

    def "Количество часов между датами"() {
        expect:
        DateUtil.between(ChronoUnit.HOURS, start, end) == result
        where:
        start                            | end                              | result
        new Date()                       | new Date()                       | 0
        sdf.parse("05.02.2015 00:00:00") | sdf.parse("06.02.2015 00:00:00") | 24
        sdf.parse("05.02.2015 00:00:00") | sdf.parse("04.02.2015 00:00:00") | -24
    }

    def "Начало сегодня"() {
        expect:
        DateUtil.generator().todayBegin() == result
        where:
        result << Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant())
    }

    def "Форматим название месяца"() {
        expect:
        DateUtil.formatter().format(date, format) == result
        where:
        date                             | format             | result
        sdf.parse("02.05.2015 00:00:00") | "«dd» MMMM yyyyг." | "«02» мая 2015г."
        sdf.parse("02.05.2015 00:00:00") | "MMMM"             | "Май"
    }

    def "Форматирование с таймзоной"() {
        expect:
        DateUtil.formatter(zoneId).format(date, format) == result
        where:
        zoneId                     | date                             | format                || result
        ZoneId.systemDefault()     | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "02.05.2015 00:00:00"
        ZoneId.of('Europe/Moscow') | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "01.05.2015 20:00:00"
    }

    def "Получение дня недели"() {
        expect:
        DateUtil.getWeekDay(year, month, day) == result
        where:
        year | month | day || result
        2015 | 8     | 10  || DayOfWeek.MONDAY
        2015 | 8     | 11  || DayOfWeek.TUESDAY
        2015 | 8     | 12  || DayOfWeek.WEDNESDAY
        2015 | 8     | 13  || DayOfWeek.THURSDAY
        2015 | 8     | 14  || DayOfWeek.FRIDAY
        2015 | 8     | 15  || DayOfWeek.SATURDAY
        2015 | 8     | 16  || DayOfWeek.SUNDAY
    }

    def "Получить номер недели в пределах года"() {
        expect:
        DateUtil.getWeekNumber(year, month, day) == result
        where:
        year | month | day || result
        2015 | 1     | 1   || 1
        2015 | 2     | 1   || 5
    }

    def "Convert Date to LocalDateTime"() {
        given:
        def date = new Date()
        when:
        def result = DateUtil.converter().get(date)
        println(date)
        println(result)
        then:
        result != null
        date.getTime() == result.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    def "Convert LocalDateTime to Date"() {
        given:
        def date = LocalDateTime.now()
        when:
        def result = DateUtil.converter().get(date)
        println(date)
        println(result)
        then:
        result != null
        result.getTime() == date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    def "Parse with default pattern"() {
        when:
        def date = DateUtil.parser().parse("27.09.1985", false)
        then:
        DateUtil.formatter().format(date, true) == "27.09.1985 00:00:00"
    }

    def "Parse with default pattern (in method)"() {
        when:
        def date = DateUtil.parser().parse("27.09.1985", false)
        then:
        DateUtil.formatter().format(date, true) == "27.09.1985 00:00:00"
    }

    def "Parse with default pattern (in method) with time"() {
        when:
        def date = DateUtil.parser().parse("27.09.1985 23:20:15", true)
        then:
        DateUtil.formatter().format(date, true) == "27.09.1985 23:20:15"
    }

    def "Parse with invalid date"() {
        when:
        def date = DateUtil.parser().parse("27.09.1985 10:20:30", false)
        then:
        DateUtil.formatter().format(date, true) == "27.09.1985 00:00:00"
    }

    def "Pretty - one year"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1)
        def result = DateUtil.pretty(false).get(date, nextDate)
        then:
        result == (date.getDayOfMonth() == 29 && date.getMonth() == Month.FEBRUARY ? '11 мес. 30 дн.' : '1 год')
    }

    def "Pretty - one year and one day"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1)
        def result = DateUtil.pretty(false).get(date, nextDate)
        then:
        result == '1 год 1 дн.'
    }

    def "Pretty - one year and one day and two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusYears(1).plusDays(1).plusHours(2)
        def result = DateUtil.pretty(false).get(date, nextDate)
        then:
        result == '1 год 1 дн. 2 ч.'
    }

    def "Pretty - two hours"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2)
        def result = DateUtil.pretty(false).get(date, nextDate)
        then:
        result == '2 ч.'
    }

    def "Pretty - two hours with minutes and seconds"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = DateUtil.pretty(false).get(date, nextDate)
        then:
        result == '2 ч. 11 м. 34 c.'
    }

    def "Pretty - two hours with minutes and seconds (use braces)"() {
        when:
        def date = LocalDateTime.now()
        def nextDate = date.plusHours(2).plusMinutes(11).plusSeconds(34)
        def result = DateUtil.pretty(true).get(date, nextDate)
        then:
        result == '(2 ч. 11 м. 34 c.)'
    }
}
