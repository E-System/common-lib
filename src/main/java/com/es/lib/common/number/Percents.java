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

package com.es.lib.common.number;

import java.math.BigDecimal;

/**
 * Calculate percent
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.11.15
 */
public final class Percents {

    private Percents() { }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(double part, double full) {
        if (full == 0.0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((part / full) * 100);
    }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(BigDecimal part, BigDecimal full) {
        if (part == null || full == null) {
            return BigDecimal.ZERO;
        }
        return value(part.doubleValue(), full.doubleValue());
    }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(int part, int full) {
        return value(part * 1.0d, full * 1.0d);
    }

    /**
     * Calculate int percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static int get(int value, double percent) {
        return (int) get((long) value, percent);
    }

    /**
     * Calculate long percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static long get(long value, double percent) {
        return Math.round(getDecimal(value, percent));
    }

    /**
     * Calculate double percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static double getDecimal(double value, double percent) {
        return value * percent / 100.0d;
    }

    /**
     * Calculate int value with percent
     *
     * @param value   Value
     * @param percent Percent
     * @return Value + value percent
     */
    public static int getTotal(int value, double percent) {
        return value + get(value, percent);
    }

    /**
     * Calculate long value with percent
     *
     * @param value   Value
     * @param percent Percent
     * @return Value + value percent
     */
    public static long getTotal(long value, double percent) {
        return value + get(value, percent);
    }
}
