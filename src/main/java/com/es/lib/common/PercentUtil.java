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
 * Рассчет суммы процента
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.11.15
 */
public class PercentUtil {

    private PercentUtil() {
    }

    /**
     * Получить целочисленный процент от числа
     *
     * @param value   число
     * @param percent процент
     * @return процент от числа
     */
    public static int get(int value, double percent) {
        return (int) Math.round(getDecimal(value, percent));
    }

    /**
     * Получить целочисленный процент от числа
     *
     * @param value   число
     * @param percent процент
     * @return процент от числа
     */
    public static long get(long value, double percent) {
        return Math.round(value * percent / 100.0d);
    }

    /**
     * Получить дробный процент от числа
     *
     * @param value   число
     * @param percent процент
     * @return процент от числа
     */
    public static double getDecimal(double value, double percent) {
        return value * percent / 100.0d;
    }

    /**
     * Получить значение + процент от значения
     *
     * @param value   значение
     * @param percent процент
     * @return значение + процент от значения
     */
    public static int getTotal(int value, double percent) {
        return value + get(value, percent);
    }

    /**
     * Получить значение + процент от значения
     *
     * @param value   значение
     * @param percent процент
     * @return значение + процент от значения
     */
    public static long getTotal(long value, double percent) {
        return value + get(value, percent);
    }
}
