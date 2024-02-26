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

package com.eslibs.common.date

import spock.lang.Shared
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 06.02.15
 */
class DatesSpec extends Specification {

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
        Dates.isZoneValid(id) == result
        where:
        id                 || result
        'Asia/Krasnoyarsk' || true
        'Europe/Moscow'    || true
        'Europe/M'         || false
    }

    def "availableZones"() {
        when:
        def zones = Dates.availableZones()
        println(zones)
        then:
        zones.contains(ZoneId.of('Asia/Krasnoyarsk'))
        zones.contains(ZoneId.of('Europe/Moscow'))
        !zones.contains(ZoneId.of('SystemV/PST8PDT'))
        !zones.contains(ZoneId.of('Zulu'))
        !zones.contains(ZoneId.of('UTC'))
    }

   /* def "NextDay"() {
        expect:
        Dates.generator(zone).nextDay(date) == result
        where:
        date                             | zone                       | result
        sdf.parse("06.02.2015 23:30:00") | ZoneId.of("Europe/Moscow") | sdf.parse("07.02.2015 23:30:00")
    }*/

    def "Contains"() {
        expect:
        Dates.contains(dbegin, dend, date)
        where:
        dbegin                                          | dend                                            | date
        LocalDateTime.parse("06.02.2015 00:00:00", dtf) | LocalDateTime.parse("07.02.3015 00:00:00", dtf) | LocalDateTime.now()
    }

    def "IsBeforeToday"() {
        expect:
        Dates.isBeforeToday(date) == result
        where:
        date                                                                 | result
        LocalDateTime.parse("01.01." + (currentYear - 1) + " 23:00:00", dtf) | true
        LocalDateTime.parse("06.02." + (currentYear + 1) + " 23:00:00", dtf) | false
        LocalDateTime.now()                                                  | false
    }

   /* def "Get start month date"() {
        expect:
        Dates.generator().monthStart() == result
        where:
        result << Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
    }*/

   /* def "Get start month date (in Moscow time zone)"() {
        expect:
        Dates.generator(zone).monthStart() == result
        where:
        zone                       | result
        ZoneId.of("Europe/Moscow") | Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant())
    }*/

   /* def "Today begin"() {
        expect:
        Dates.generator().today() == result
        where:
        result << Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant())
    }*/

   /* def "Format months"() {
        expect:
        Dates.formatter().format(date, format) == result
        where:
        date                             | format             | result
        sdf.parse("02.05.2015 00:00:00") | "«dd» MMMM yyyyг." | "«02» мая 2015г."
        sdf.parse("02.05.2015 00:00:00") | "MMMM"             | "май"
    }*/

   /* def "Format with time zone"() {
        expect:
        Dates.formatter(zoneId).format(date, format) == result
        where:
        zoneId                     | date                             | format                || result
        ZoneId.systemDefault()     | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "02.05.2015 00:00:00"
        ZoneId.of('Europe/Moscow') | sdf.parse("02.05.2015 00:00:00") | "dd.MM.yyyy HH:mm:ss" || "01.05.2015 20:00:00"
    }*/

    /*def "Get week number in year"() {
        expect:
        Dates.getWeekNumber(year, month, day) == result
        where:
        year | month | day || result
        2015 | 1     | 1   || 1
        2015 | 2     | 1   || 5
    }*/

    def "Convert Date to LocalDateTime"() {
        given:
        def date = new Date()
        when:
        def result = Dates.converter().get(date)
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
        def result = Dates.converter().get(date)
        println(date)
        println(result)
        then:
        result != null
        result.getTime() == date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    /* def "Parse with default pattern"() {
         when:
         def date = Dates.parser().parse("27.09.1985", false)
         then:
         Dates.formatter().format(date, true) == "27.09.1985 00:00:00"
     }*/

    /*def "Parse with default pattern (in method)"() {
        when:
        def date = Dates.parser().parse("27.09.1985", false)
        then:
        Dates.formatter().format(date, true) == "27.09.1985 00:00:00"
    }*/

    /* def "Parse with default pattern (in method) with time"() {
         when:
         def date = Dates.parser().parse("27.09.1985 23:20:15", true)
         then:
         Dates.formatter().format(date, true) == "27.09.1985 23:20:15"
     }*/

    /* def "Parse with invalid date"() {
         when:
         def date = Dates.parser().parse("27.09.1985 10:20:30", false)
         then:
         Dates.formatter().format(date, true) == "27.09.1985 00:00:00"
     }*/

    def "Date diff"() {
        when:
        def now = LocalDateTime.now()
        def start = now.minusDays(1)
        then:
        ChronoUnit.SECONDS.between(start, now) == 86400
    }

    def "Date diff with timezone"() {
        when:
        def zoneId = ZoneId.of("Europe/Moscow")
        def now = LocalDateTime.now(zoneId)
        def start = now.minusDays(1)
        then:
        ChronoUnit.SECONDS.between(start, now) == 86400
    }

    def "Date diff with timezone fow now"() {
        when:
        def zoneId = ZoneId.of("Europe/Moscow")
        def now = LocalDateTime.now(zoneId)
        def start = now.minusDays(1)
        then:
        ChronoUnit.SECONDS.between(start, LocalDateTime.now(zoneId)) == 86400
    }

    def "Calendar with current year"() {
        when:
        def startOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        def calendar = Dates.calendar(startOfYear, 1)
        then:
        calendar.size() == 1
        calendar[startOfYear.getYear()].size() == 12
        with(calendar[startOfYear.getYear()]) {
            it[1].size() == YearMonth.of(startOfYear.getYear(), 1).lengthOfMonth()
            it[2].size() == YearMonth.of(startOfYear.getYear(), 2).lengthOfMonth()
            it[3].size() == YearMonth.of(startOfYear.getYear(), 3).lengthOfMonth()
            it[4].size() == YearMonth.of(startOfYear.getYear(), 4).lengthOfMonth()
            it[5].size() == YearMonth.of(startOfYear.getYear(), 5).lengthOfMonth()
            it[6].size() == YearMonth.of(startOfYear.getYear(), 6).lengthOfMonth()
            it[7].size() == YearMonth.of(startOfYear.getYear(), 7).lengthOfMonth()
            it[8].size() == YearMonth.of(startOfYear.getYear(), 8).lengthOfMonth()
            it[9].size() == YearMonth.of(startOfYear.getYear(), 9).lengthOfMonth()
            it[10].size() == YearMonth.of(startOfYear.getYear(), 10).lengthOfMonth()
            it[11].size() == YearMonth.of(startOfYear.getYear(), 11).lengthOfMonth()
            it[12].size() == YearMonth.of(startOfYear.getYear(), 12).lengthOfMonth()
        }
    }

    def "Calendar with two years"() {
        when:
        def startOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        def calendar = Dates.calendar(startOfYear, 2)
        then:
        calendar.size() == 2
        calendar[startOfYear.getYear()].size() == 12
        calendar[startOfYear.getYear() + 1].size() == 12
        with(calendar[startOfYear.getYear()]) {
            it[1].size() == YearMonth.of(startOfYear.getYear(), 1).lengthOfMonth()
            it[2].size() == YearMonth.of(startOfYear.getYear(), 2).lengthOfMonth()
            it[3].size() == YearMonth.of(startOfYear.getYear(), 3).lengthOfMonth()
            it[4].size() == YearMonth.of(startOfYear.getYear(), 4).lengthOfMonth()
            it[5].size() == YearMonth.of(startOfYear.getYear(), 5).lengthOfMonth()
            it[6].size() == YearMonth.of(startOfYear.getYear(), 6).lengthOfMonth()
            it[7].size() == YearMonth.of(startOfYear.getYear(), 7).lengthOfMonth()
            it[8].size() == YearMonth.of(startOfYear.getYear(), 8).lengthOfMonth()
            it[9].size() == YearMonth.of(startOfYear.getYear(), 9).lengthOfMonth()
            it[10].size() == YearMonth.of(startOfYear.getYear(), 10).lengthOfMonth()
            it[11].size() == YearMonth.of(startOfYear.getYear(), 11).lengthOfMonth()
            it[12].size() == YearMonth.of(startOfYear.getYear(), 12).lengthOfMonth()
        }
        with(calendar[startOfYear.getYear() + 1]) {
            it[1].size() == YearMonth.of(startOfYear.getYear() + 1, 1).lengthOfMonth()
            it[2].size() == YearMonth.of(startOfYear.getYear() + 1, 2).lengthOfMonth()
            it[3].size() == YearMonth.of(startOfYear.getYear() + 1, 3).lengthOfMonth()
            it[4].size() == YearMonth.of(startOfYear.getYear() + 1, 4).lengthOfMonth()
            it[5].size() == YearMonth.of(startOfYear.getYear() + 1, 5).lengthOfMonth()
            it[6].size() == YearMonth.of(startOfYear.getYear() + 1, 6).lengthOfMonth()
            it[7].size() == YearMonth.of(startOfYear.getYear() + 1, 7).lengthOfMonth()
            it[8].size() == YearMonth.of(startOfYear.getYear() + 1, 8).lengthOfMonth()
            it[9].size() == YearMonth.of(startOfYear.getYear() + 1, 9).lengthOfMonth()
            it[10].size() == YearMonth.of(startOfYear.getYear() + 1, 10).lengthOfMonth()
            it[11].size() == YearMonth.of(startOfYear.getYear() + 1, 11).lengthOfMonth()
            it[12].size() == YearMonth.of(startOfYear.getYear() + 1, 12).lengthOfMonth()
        }
    }

    def "Days"() {
        when:
        def from = LocalDate.now()
        def to = from.plusDays(7)
        def result = Dates.days(from, to, null)
        then:
        result.size() == 7
        result[0] == from
        result[1] == from.plusDays(1)
        result[2] == from.plusDays(2)
        result[3] == from.plusDays(3)
        result[4] == from.plusDays(4)
        result[5] == from.plusDays(5)
        result[6] == from.plusDays(6)
        result[6] == to.minusDays(1)
    }

    def "Days for month"() {
        when:
        def from = LocalDate.of(2021, 4, 1)
        def to = from.plusMonths(1)
        def result = Dates.days(from, to, null)
        then:
        result.size() == 30
        result[0] == from
        result[29] == to.minusDays(1)
    }

    def "Days with filter"() {
        when:
        def from = LocalDate.of(2021, 4, 1)
        def to = from.plusDays(7)
        def result = Dates.days(from, to, { it.getDayOfWeek() == DayOfWeek.MONDAY })
        then:
        result.size() == 1
        result[0] == from.plusDays(4)
    }
}
