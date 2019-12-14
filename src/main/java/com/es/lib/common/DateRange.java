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

package com.es.lib.common;

import com.es.lib.common.builder.DateBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Data
@AllArgsConstructor
public class DateRange {

    private Date dbegin;
    private Date dend;

    public static Collection<SItem> getAll(TimeZone timeZone, boolean lastNextDay) {
        return Stream.of(DateRange.Interval.values())
                     .map(v -> v.getItem(timeZone, lastNextDay))
                     .collect(Collectors.toList());
    }

    public static Collection<SItem> getAll(TimeZone timeZone) {
        return getAll(timeZone, true);
    }

    public static DateRange getDefaultRange(TimeZone timeZone) {
        return getDefaultRange(timeZone, true);
    }

    public static DateRange getDefaultRange(TimeZone timeZone, boolean lastNextDay) {
        return Interval.TODAY.getRange(timeZone, lastNextDay);
    }

    public String getIntervalString() {
        return DateUtil.format(getDbegin(), DateUtil.CALENDAR_DATE_PATTERN) + "|" + DateUtil.format(getDend(), DateUtil.CALENDAR_DATE_PATTERN);
    }

    public enum Interval {
        TODAY {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().build(),
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(lastNextDay ? 1 : 0).build()
                );
            }
        },
        YESTERDAY {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(-1).build(),
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        },
        LAST_7_DAYS {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(-7).build(),
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(lastNextDay ? 1 : 0).build()
                );
            }
        },
        CURRENT_WEEK {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfWeek(Calendar.MONDAY).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(lastNextDay ? 1 : 0).build()
                );
            }
        },
        LAST_WEEK {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.MONDAY).build(),
                    DateBuilder.create(timeZone).clearTime().addWeekOfMonth(-1).setDayOfWeek(Calendar.SUNDAY).addDayOfMonth(lastNextDay ? 1 : 0).build()
                );
            }
        },
        CURRENT_MONTH {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).addMonth(1).addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        },
        LAST_MONTH {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().addMonth(-1).setDayOfMonth(1).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        },
        CURRENT_TRIAD {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).addMonth(3).addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        },
        LAST_TRIAD {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).addMonth(-3).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        },
        CURRENT_YEAR {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).setMonth(0).build(),
                    DateBuilder.create(timeZone).clearTime().addDayOfMonth(lastNextDay ? 1 : 0).build()
                );
            }
        },
        LAST_YEAR {
            @Override
            public DateRange getRange(TimeZone timeZone, boolean lastNextDay) {
                return new DateRange(
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).setMonth(0).addYear(-1).build(),
                    DateBuilder.create(timeZone).clearTime().setDayOfMonth(1).setMonth(0).addDayOfMonth(lastNextDay ? 0 : -1).build()
                );
            }
        };

        public DateRange getRange(TimeZone timeZone) {
            return getRange(timeZone, true);
        }

        public abstract DateRange getRange(TimeZone timeZone, boolean lastNextDay);

        public SItem getItem(TimeZone timeZone) {
            return getItem(timeZone, true);
        }

        public SItem getItem(TimeZone timeZone, boolean lastNextDay) {
            return new SItem(this.getRange(timeZone, lastNextDay).getIntervalString(), this.toString());
        }

    }
}
