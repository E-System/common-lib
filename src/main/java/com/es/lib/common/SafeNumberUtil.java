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
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 18.07.15
 */
public class SafeNumberUtil {

    private SafeNumberUtil() {
    }

    /**
     * Получить значение value если value != null, иначе defValue
     *
     * @param value    число
     * @param defValue число по умолчанию
     * @return значение value если value != null, иначе defValue
     */
    public static double getDouble(Number value, double defValue) {
        return value != null ? value.doubleValue() : defValue;
    }

    /**
     * Получить значение value если value != null, иначе 0.0d
     *
     * @param value число
     * @return значение value если value != null, иначе 0.0d
     */
    public static double getDouble(Number value) {
        return getDouble(value, 0.0d);
    }

    /**
     * Получить значение value если value != null, иначе defValue
     *
     * @param value    число
     * @param defValue число по умолчанию
     * @return значение value если value != null, иначе defValue
     */
    public static long getLong(Number value, long defValue) {
        return value != null ? value.longValue() : defValue;
    }

    /**
     * Получить значение value если value != null, иначе 0L
     *
     * @param value число
     * @return значение value если value != null, иначе 0L
     */
    public static long getLong(Number value) {
        return getLong(value, 0L);
    }

    /**
     * Получить значение value если value != null, иначе defValue
     *
     * @param value    число
     * @param defValue число по умолчанию
     * @return значение value если value != null, иначе defValue
     */
    public static short getShort(Number value, short defValue) {
        return value != null ? value.shortValue() : defValue;
    }

    /**
     * Получить значение value если value != null, иначе 0
     *
     * @param value число
     * @return значение value если value != null, иначе 0
     */
    public static short getShort(Number value) {
        return getShort(value, (short) 0);
    }

    /**
     * Получить значение value если value != null, иначе defValue
     *
     * @param value    число
     * @param defValue число по умолчанию
     * @return значение value если value != null, иначе defValue
     */
    public static int getInt(Number value, int defValue) {
        return value != null ? value.intValue() : defValue;
    }

    /**
     * Получить значение value если value != null, иначе 0
     *
     * @param value число
     * @return значение value если value != null, иначе 0
     */
    public static int getInt(Number value) {
        return getInt(value, 0);
    }

    /**
     * Получить значение value если value != null, иначе null
     *
     * @param value число
     * @return значение value если value != null, иначе null
     */
    public static Short getShortObject(Number value) {
        return value != null ? value.shortValue() : null;
    }

    /**
     * Получить значение value если value != null, иначе null
     *
     * @param value число
     * @return значение value если value != null, иначе null
     */
    public static Integer getIntObject(Number value) {
        return value != null ? value.intValue() : null;
    }

    /**
     * Получить значение value если value != null, иначе null
     *
     * @param value число
     * @return значение value если value != null, иначе null
     */
    public static Long getLongObject(Number value) {
        return value != null ? value.longValue() : null;
    }

    /**
     * Получить значение value если value != null, иначе null
     *
     * @param value число
     * @return значение value если value != null, иначе null
     */
    public static Double getDoubleObject(Number value) {
        return value != null ? value.doubleValue() : null;
    }
}
