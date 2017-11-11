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

package com.es.lib.common.builder;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class DateBuilder {

    private final Calendar calendar;

    public DateBuilder(TimeZone timeZone) {
        calendar = Calendar.getInstance(timeZone);
    }

    private DateBuilder(Calendar calendar) {
        this.calendar = calendar;
    }

    public static DateBuilder create(TimeZone timeZone) {
        return new DateBuilder(timeZone);
    }

    public static DateBuilder create(Calendar calendar) {
        return new DateBuilder(calendar);
    }

    public DateBuilder clearTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return this;
    }

    public DateBuilder addDayOfMonth(int days) {
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return this;
    }

    public DateBuilder setDayOfMonth(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return this;
    }

    public DateBuilder setDayOfWeek(int day) {
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return this;
    }

    public DateBuilder addDayOfWeek(int days) {
        calendar.add(Calendar.DAY_OF_WEEK, days);
        return this;
    }

    public DateBuilder addWeekOfMonth(int weeks) {
        calendar.add(Calendar.WEEK_OF_MONTH, weeks);
        return this;
    }

    public DateBuilder setWeekOfMonth(int week) {
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        return this;
    }

    public DateBuilder addMonth(int months) {
        calendar.add(Calendar.MONTH, months);
        return this;
    }

    public DateBuilder setMonth(int month) {
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    public DateBuilder addYear(int year) {
        calendar.add(Calendar.YEAR, year);
        return this;
    }

    public DateBuilder setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public Date build() {
        return calendar.getTime();
    }
}
