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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DateRange {

    private final Dates.Environment environment;
    private final LocalDate start;
    private final LocalDate end;

    public String toKey() {
        return toKey(environment);
    }

    public String toKey(Dates.Environment environment) {
        DateTimeFormatter dateFormatter = environment.getDateRangeFormatter();
        return dateFormatter.format(getStart()) + "|" + dateFormatter.format(getEnd());
    }

    public enum Interval {
        TODAY {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        YESTERDAY {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).minusDays(1);
                return new DateRange(
                    environment,
                    begin,
                    begin.plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        LAST_7_DAYS {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId());
                return new DateRange(
                    environment,
                    begin.minusDays(7),
                    begin.plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        CURRENT_WEEK {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId());
                return new DateRange(
                    environment,
                    begin.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    begin.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        LAST_WEEK {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).minusWeeks(1);
                return new DateRange(
                    environment,
                    begin.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    begin.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        CURRENT_MONTH {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).with(TemporalAdjusters.firstDayOfMonth());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusMonths(1).plusDays(environment.isLastNextDay() ? 0 : -1)
                );
            }
        },
        LAST_MONTH {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusMonths(1).plusDays(environment.isLastNextDay() ? 0 : -1)
                );
            }
        },
        CURRENT_TRIAD {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).with(TemporalAdjusters.firstDayOfMonth());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusMonths(3).plusDays(environment.isLastNextDay() ? 0 : -1)
                );
            }
        },
        LAST_TRIAD {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusMonths(3).plusDays(environment.isLastNextDay() ? 0 : -1)
                );
            }
        },
        CURRENT_YEAR {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).with(TemporalAdjusters.firstDayOfYear());
                return new DateRange(
                    environment,
                    begin,
                    LocalDate.now(environment.getZoneId()).plusDays(environment.isLastNextDay() ? 1 : 0)
                );
            }
        },
        LAST_YEAR {
            @Override
            public DateRange getRange(Dates.Environment environment) {
                LocalDate begin = LocalDate.now(environment.getZoneId()).minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                return new DateRange(
                    environment,
                    begin,
                    begin.plusYears(1).plusDays(environment.isLastNextDay() ? 0 : -1)
                );
            }
        };

        public DateRange getRange() {
            return getRange(Dates.getEnvironment());
        }

        public abstract DateRange getRange(Dates.Environment environment);

        public SItem toItem(Dates.Environment environment) {
            return new SItem(getRange(environment).toKey(), this.toString());
        }
    }
}
