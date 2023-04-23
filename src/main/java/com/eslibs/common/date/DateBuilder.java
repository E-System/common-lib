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

package com.eslibs.common.date;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class DateBuilder {

    private final Calendar calendar;

    DateBuilder(ZoneId zoneId) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone(zoneId));
    }

    public DateBuilder clearTime() {
        return set(Calendar.HOUR_OF_DAY, 0)
            .set(Calendar.MINUTE, 0)
            .set(Calendar.SECOND, 0)
            .set(Calendar.MILLISECOND, 0);
    }

    public DateBuilder addDayOfMonth(int days) {
        return add(Calendar.DAY_OF_MONTH, days);
    }

    public DateBuilder setDayOfMonth(int day) {
        return set(Calendar.DAY_OF_MONTH, day);
    }

    public DateBuilder setDayOfWeek(int day) {
        return set(Calendar.DAY_OF_WEEK, day);
    }

    public DateBuilder addDayOfWeek(int days) {
        return add(Calendar.DAY_OF_WEEK, days);
    }

    public DateBuilder addWeekOfMonth(int weeks) {
        return add(Calendar.WEEK_OF_MONTH, weeks);
    }

    public DateBuilder setWeekOfMonth(int week) {
        return set(Calendar.WEEK_OF_MONTH, week);
    }

    public DateBuilder addMonth(int months) {
        return add(Calendar.MONTH, months);
    }

    public DateBuilder setMonth(int month) {
        return set(Calendar.MONTH, month);
    }

    public DateBuilder addYear(int year) {
        return add(Calendar.YEAR, year);
    }

    public DateBuilder setYear(int year) {
        return set(Calendar.YEAR, year);
    }

    public DateBuilder setHourOfDay(Integer hour) {
        return set(Calendar.HOUR_OF_DAY, hour != null ? hour : 0);
    }

    public DateBuilder add(int field, int amount) {
        calendar.add(field, amount);
        return this;
    }

    public DateBuilder set(int field, Integer amount) {
        calendar.set(field, amount != null ? amount : 0);
        return this;
    }

    public Date build() {
        return calendar.getTime();
    }
}
