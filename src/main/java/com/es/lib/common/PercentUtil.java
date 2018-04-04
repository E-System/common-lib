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

/**
 * Calculate percent
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.11.15
 */
public class PercentUtil {

    private PercentUtil() {
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
