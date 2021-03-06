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

import spock.lang.Specification

import java.time.ZoneId

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class DateRangeSpec extends Specification {

    def "Interval generate with next day"() {
        expect:
        def range = interval.getRange(zoneId)
        range.dbegin == dbegin
        range.dend == dend
        where:
        interval                         | zoneId                 | dbegin                                                                                  | dend
        DateRange.Interval.TODAY         | ZoneId.systemDefault() | Dates.builder().clearTime().build()                                                  | Dates.builder().clearTime().addDayOfMonth(1).build()
        DateRange.Interval.YESTERDAY     | ZoneId.systemDefault() | Dates.builder().clearTime().addDayOfMonth(-1).build()                                | Dates.builder().clearTime().build()
        DateRange.Interval.LAST_7_DAYS   | ZoneId.systemDefault() | Dates.builder().clearTime().addDayOfMonth(-7).build()                                | Dates.builder().clearTime().addDayOfMonth(1).build()
        DateRange.Interval.CURRENT_WEEK  | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfWeek(Calendar.MONDAY).build()                    | Dates.builder().clearTime().setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(1).build()
        DateRange.Interval.LAST_WEEK     | ZoneId.systemDefault() | Dates.builder().clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.MONDAY).build() | Dates.builder().clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(1).build()
        DateRange.Interval.CURRENT_MONTH | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).build()                                 | Dates.builder().clearTime().setDayOfMonth(1).addMonth(1).build()
        DateRange.Interval.LAST_MONTH    | ZoneId.systemDefault() | Dates.builder().clearTime().addMonth(-1).setDayOfMonth(1).build()                    | Dates.builder().clearTime().setDayOfMonth(1).build()
        DateRange.Interval.CURRENT_TRIAD | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).build()                                 | Dates.builder().clearTime().setDayOfMonth(1).addMonth(3).build()
        DateRange.Interval.LAST_TRIAD    | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).addMonth(-3).build()                    | Dates.builder().clearTime().setDayOfMonth(1).build()
        DateRange.Interval.CURRENT_YEAR  | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).build()                     | Dates.builder().clearTime().addDayOfMonth(1).build()
        DateRange.Interval.LAST_YEAR     | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).addYear(-1).build()         | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).build()
    }

    def "Interval generate with current day"() {
        expect:
        def range = interval.getRange(zoneId, false)
        range.dbegin == dbegin
        range.dend == dend
        where:
        interval                         | zoneId                 | dbegin                                                                                  | dend
        DateRange.Interval.TODAY         | ZoneId.systemDefault() | Dates.builder().clearTime().build()                                                  | Dates.builder().clearTime().addDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.YESTERDAY     | ZoneId.systemDefault() | Dates.builder().clearTime().addDayOfMonth(-1).build()                                | Dates.builder().clearTime().addDayOfMonth(-1).build()
        DateRange.Interval.LAST_7_DAYS   | ZoneId.systemDefault() | Dates.builder().clearTime().addDayOfMonth(-7).build()                                | Dates.builder().clearTime().addDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.CURRENT_WEEK  | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfWeek(Calendar.MONDAY).build()                    | Dates.builder().clearTime().setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.LAST_WEEK     | ZoneId.systemDefault() | Dates.builder().clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.MONDAY).build() | Dates.builder().clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.CURRENT_MONTH | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).build()                                 | Dates.builder().clearTime().setDayOfMonth(1).addMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.LAST_MONTH    | ZoneId.systemDefault() | Dates.builder().clearTime().addMonth(-1).setDayOfMonth(1).build()                    | Dates.builder().clearTime().setDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.CURRENT_TRIAD | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).build()                                 | Dates.builder().clearTime().setDayOfMonth(1).addMonth(3).addDayOfMonth(-1).build()
        DateRange.Interval.LAST_TRIAD    | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).addMonth(-3).build()                    | Dates.builder().clearTime().setDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.CURRENT_YEAR  | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).build()                     | Dates.builder().clearTime().addDayOfMonth(1).addDayOfMonth(-1).build()
        DateRange.Interval.LAST_YEAR     | ZoneId.systemDefault() | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).addYear(-1).build()         | Dates.builder().clearTime().setDayOfMonth(1).setMonth(0).addDayOfMonth(-1).build()
    }

    def "Generate list of all intervals"() {
        when:
        def ranges = Dates.ranges(ZoneId.systemDefault())
        then:
        ranges.size() == DateRange.Interval.values().length
        ranges[0].title == DateRange.Interval.TODAY.toString()
        ranges[1].title == DateRange.Interval.YESTERDAY.toString()
        ranges[2].title == DateRange.Interval.LAST_7_DAYS.toString()
        ranges[3].title == DateRange.Interval.CURRENT_WEEK.toString()
        ranges[4].title == DateRange.Interval.LAST_WEEK.toString()
        ranges[5].title == DateRange.Interval.CURRENT_MONTH.toString()
        ranges[6].title == DateRange.Interval.LAST_MONTH.toString()
        ranges[7].title == DateRange.Interval.CURRENT_TRIAD.toString()
        ranges[8].title == DateRange.Interval.LAST_TRIAD.toString()
        ranges[9].title == DateRange.Interval.CURRENT_YEAR.toString()
        ranges[10].title == DateRange.Interval.LAST_YEAR.toString()
    }
}
