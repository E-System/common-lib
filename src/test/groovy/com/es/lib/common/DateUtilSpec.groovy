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

package com.es.lib.common


import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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

    def "NextDay"() {
        expect:
        DateUtil.nextDay(date, zone) == result
        where:
        date                             | zone                                  | result
        sdf.parse("06.02.2015 23:30:00") | TimeZone.getTimeZone("Europe/Moscow") | sdf.parse("07.02.2015 23:30:00")
    }

    def "Contains"() {
        expect:
        DateUtil.contains(dbegin, dend, date, timeZone)
        where:
        dbegin                           | dend                             | date       | timeZone
        sdf.parse("06.02.2015 00:00:00") | sdf.parse("07.02.3015 00:00:00") | new Date() | TimeZone.getTimeZone("Europe/Moscow")
    }

    def "IsAfterNow"() {
        expect:
        DateUtil.isAfterNow(date, timeZone) == result
        where:
        date                                                  | timeZone                              | result
        new Date()                                            | TimeZone.default                      | false
        sdf.parse("06.02." + (currentYear + 1) + " 23:00:00") | TimeZone.default                      | true
        sdf.parse("05.02." + (currentYear - 1) + " 23:00:00") | TimeZone.default                      | false
        new Date()                                            | TimeZone.getTimeZone("Europe/Moscow") | false
        sdf.parse("06.02." + (currentYear + 1) + " 23:00:00") | TimeZone.getTimeZone("Europe/Moscow") | true
        sdf.parse("05.02." + (currentYear - 1) + " 23:00:00") | TimeZone.getTimeZone("Europe/Moscow") | false
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
        DateUtil.monthStart() == result
        where:
        result << Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    def "Получение даты в начале месяца (в Москве)"() {
        expect:
        DateUtil.monthStart() == result
        where:
        zone                       | result
        ZoneId.of("Europe/Moscow") | Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
        DateUtil.todayBegin() == result
        where:
        result << Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
    }

    def "Форматим название месяца"() {
        expect:
        DateUtil.format(date, format) == result
        where:
        date                             | format             | result
        sdf.parse("02.05.2015 00:00:00") | "«dd» MMMM yyyyг." | "«02» мая 2015г."
        sdf.parse("02.05.2015 00:00:00") | "MMMM"             | "май"
    }

    def "Форматирование с таймзоной"() {
        expect:
        DateUtil.format(timeZone, date, format) == result
        where:
        timeZone                              | date                             | format                || result
        TimeZone.default                      | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "02.05.2015 00:00:00"
        TimeZone.getTimeZone('Europe/Moscow') | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "01.05.2015 20:00:00"
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
        def result = DateUtil.convert(date)
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
        def result = DateUtil.convert(date)
        println(date)
        println(result)
        then:
        result != null
        result.getTime() == date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    def "Parse with default pattern"() {
        when:
        def date = DateUtil.parse("27.09.1985", DateUtil.CALENDAR_DATE_PATTERN)
        then:
        DateUtil.format(date, DateUtil.CALENDAR_DATE_PATTERN_WITH_TIME) == "27.09.1985 00:00:00"
    }

    def "Parse with default pattern (in method)"() {
        when:
        def date = DateUtil.parse("27.09.1985")
        then:
        DateUtil.format(date, DateUtil.CALENDAR_DATE_PATTERN_WITH_TIME) == "27.09.1985 00:00:00"
    }

    def "Parse with default pattern (in method) with time"() {
        when:
        def date = DateUtil.parseWithTime("27.09.1985 23:20:15")
        then:
        DateUtil.format(date, DateUtil.CALENDAR_DATE_PATTERN_WITH_TIME) == "27.09.1985 23:20:15"
    }

    def "Parse with invalid date"() {
        when:
        def date = DateUtil.parse("27.09.1985 10:20:30", DateUtil.CALENDAR_DATE_PATTERN)
        then:
        DateUtil.format(date, DateUtil.CALENDAR_DATE_PATTERN_WITH_TIME) == "27.09.1985 00:00:00"
    }
}
