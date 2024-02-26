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

import com.eslibs.common.model.SItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DateRange {

    private final LocalDateTime dbegin;
    private final LocalDateTime dend;

    public String getIntervalString() {
        return getIntervalString(null);
    }

    public String getIntervalString(DateTimeFormatter dateTimeFormatter) {
        DateTimeFormatter formatter = dateTimeFormatter != null ? dateTimeFormatter : Dates.getEnvironment().getDateFormatter();
        return formatter.format(getDbegin()) + "|" + formatter.format(getDend());
    }

    public enum Interval {
        TODAY {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        YESTERDAY {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS).minusDays(1);
                return new DateRange(
                    begin,
                    begin.plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        LAST_7_DAYS {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin.minusDays(7),
                    begin.plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        CURRENT_WEEK {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    begin.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        LAST_WEEK {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS).minusWeeks(1);
                return new DateRange(
                    begin.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    begin.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        CURRENT_MONTH {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusMonths(1).plusDays(lastNextDay ? 0 : -1)
                );
            }
        },
        LAST_MONTH {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusMonths(1).plusDays(lastNextDay ? 0 : -1)
                );
            }
        },
        CURRENT_TRIAD {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusMonths(3).plusDays(lastNextDay ? 0 : -1)
                );
            }
        },
        LAST_TRIAD {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusMonths(3).plusDays(lastNextDay ? 0 : -1)
                );
            }
        },
        CURRENT_YEAR {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).with(TemporalAdjusters.firstDayOfYear()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    LocalDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS).plusDays(lastNextDay ? 1 : 0)
                );
            }
        },
        LAST_YEAR {
            @Override
            public DateRange getRange(ZoneId zoneId, boolean lastNextDay) {
                LocalDateTime begin = LocalDateTime.now(zoneId).minusYears(1).with(TemporalAdjusters.firstDayOfYear()).truncatedTo(ChronoUnit.DAYS);
                return new DateRange(
                    begin,
                    begin.plusYears(1).plusDays(lastNextDay ? 0 : -1)
                );
            }
        };

        public DateRange getRange(ZoneId zoneId) {
            return getRange(zoneId, true);
        }

        public abstract DateRange getRange(ZoneId zoneId, boolean lastNextDay);

        public SItem getItem(ZoneId zoneId) {
            return getItem(zoneId, true);
        }

        public SItem getItem(ZoneId zoneId, DateTimeFormatter dateTimeFormatter) {
            return getItem(zoneId, dateTimeFormatter, true);
        }

        public SItem getItem(ZoneId zoneId, boolean lastNextDay) {
            return getItem(zoneId, null, lastNextDay);
        }

        public SItem getItem(ZoneId zoneId, DateTimeFormatter dateTimeFormatter, boolean lastNextDay) {
            return new SItem(this.getRange(zoneId, lastNextDay).getIntervalString(dateTimeFormatter), this.toString());
        }
    }
}
