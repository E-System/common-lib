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

package com.es.lib.common.validation;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Получить дробное число из объекта(toString)
     *
     * @param value объект для получения числа
     * @return дробное число
     */
    public static double parseDouble(Object value) {
        return parseDouble(value.toString());
    }

    /**
     * Проверить корректность введенного дробного числа
     *
     * @param value объект для получения числа
     * @return true - строка корректно парсится в дробное число
     */
    public static boolean isValidDouble(Object value) {
        try {
            parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Получить дробное число из строки
     *
     * @param value строка для получения числа
     * @return дробное число
     */
    public static double parseDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    /**
     * Получить целочисленную сумму из введенной строки дробного числа(с копейками)
     *
     * @param value строка дробного числа(с копейками)
     * @return целочисленная сумма Math.round(value * 100)
     */
    public static long parseSum(String value) {
        return Math.round(parseDouble(value) * 100);
    }

    /**
     * Получить целое число из объекта(toString)
     *
     * @param value объект для получения числа
     * @return целое число
     */
    public static long parseLong(Object value) {
        return parseLong(value.toString());
    }

    /**
     * Проверить корректность введенного целого числа
     *
     * @param value объект для получения числа
     * @return true - строка корректно парсится в целое число
     */
    public static boolean isValidLong(Object value) {
        try {
            parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Получить целое число из строки
     *
     * @param value строка для получения числа
     * @return целое число
     */
    public static long parseLong(String value) {
        return Long.parseLong(value);
    }

    /**
     * Get int representation value character at index
     *
     * @param value string
     * @param index index of char
     * @return int representation
     */
    public static int getInt(String value, int index) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (index < 0 || value.length() < index) {
            throw new IllegalArgumentException();
        }
        return Character.getNumericValue(value.charAt(index));
    }
}
