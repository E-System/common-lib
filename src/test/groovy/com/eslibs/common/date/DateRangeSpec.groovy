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

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class DateRangeSpec extends Specification {

    @Shared
    def defEnv = Dates.environment
    @Shared
    def defEnvCurDay = Dates.environment.toBuilder().lastNextDay(false).build()

    def "Interval generate with next day"() {
        expect:
        def range = interval.getRange(env)
        range.start == start
        range.end == end
        where:
        interval                         | env    | start                                                                                  | end
        DateRange.Interval.TODAY         | defEnv | LocalDate.now()                                                                        | LocalDate.now().plusDays(1)
        DateRange.Interval.YESTERDAY     | defEnv | LocalDate.now().minusDays(1)                                                           | LocalDate.now()
        DateRange.Interval.LAST_7_DAYS   | defEnv | LocalDate.now().minusDays(7)                                                           | LocalDate.now().plusDays(1)
        DateRange.Interval.CURRENT_WEEK  | defEnv | LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))               | LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1)
        DateRange.Interval.LAST_WEEK     | defEnv | LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) | LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1)
        DateRange.Interval.CURRENT_MONTH | defEnv | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())                              | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1)
        DateRange.Interval.LAST_MONTH    | defEnv | LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())               | LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1)
        DateRange.Interval.CURRENT_TRIAD | defEnv | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())                              | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(3)
        DateRange.Interval.LAST_TRIAD    | defEnv | LocalDate.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth())               | LocalDate.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()).plusMonths(3)
        DateRange.Interval.CURRENT_YEAR  | defEnv | LocalDate.now().with(TemporalAdjusters.firstDayOfYear())                               | LocalDate.now().plusDays(1)
        DateRange.Interval.LAST_YEAR     | defEnv | LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear())                 | LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear()).plusYears(1)
    }

    def "Interval generate with current day"() {
        expect:
        def range = interval.getRange(env)
        range.start == start
        range.end == end
        where:
        interval                         | env          | start                                                                                  | end
        DateRange.Interval.TODAY         | defEnvCurDay | LocalDate.now()                                                                        | LocalDate.now()
        DateRange.Interval.YESTERDAY     | defEnvCurDay | LocalDate.now().minusDays(1)                                                           | LocalDate.now().minusDays(1)
        DateRange.Interval.LAST_7_DAYS   | defEnvCurDay | LocalDate.now().minusDays(7)                                                           | LocalDate.now()
        DateRange.Interval.CURRENT_WEEK  | defEnvCurDay | LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))               | LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        DateRange.Interval.LAST_WEEK     | defEnvCurDay | LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) | LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        DateRange.Interval.CURRENT_MONTH | defEnvCurDay | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())                              | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1).minusDays(1)
        DateRange.Interval.LAST_MONTH    | defEnvCurDay | LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())               | LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1).minusDays(1)
        DateRange.Interval.CURRENT_TRIAD | defEnvCurDay | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())                              | LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusMonths(3).minusDays(1)
        DateRange.Interval.LAST_TRIAD    | defEnvCurDay | LocalDate.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth())               | LocalDate.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()).plusMonths(3).minusDays(1)
        DateRange.Interval.CURRENT_YEAR  | defEnvCurDay | LocalDate.now().with(TemporalAdjusters.firstDayOfYear())                               | LocalDate.now()
        DateRange.Interval.LAST_YEAR     | defEnvCurDay | LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear())                 | LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear()).plusYears(1).minusDays(1)
    }

    def "Generate list of all intervals"() {
        when:
        def ranges = Dates.ranges()
        then:
        ranges.size() == DateRange.Interval.values().length
        ranges[0].title() == DateRange.Interval.TODAY.toString()
        ranges[1].title() == DateRange.Interval.YESTERDAY.toString()
        ranges[2].title() == DateRange.Interval.LAST_7_DAYS.toString()
        ranges[3].title() == DateRange.Interval.CURRENT_WEEK.toString()
        ranges[4].title() == DateRange.Interval.LAST_WEEK.toString()
        ranges[5].title() == DateRange.Interval.CURRENT_MONTH.toString()
        ranges[6].title() == DateRange.Interval.LAST_MONTH.toString()
        ranges[7].title() == DateRange.Interval.CURRENT_TRIAD.toString()
        ranges[8].title() == DateRange.Interval.LAST_TRIAD.toString()
        ranges[9].title() == DateRange.Interval.CURRENT_YEAR.toString()
        ranges[10].title() == DateRange.Interval.LAST_YEAR.toString()
    }
}
