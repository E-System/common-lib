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
 * @since 10.04.15
 */
public final class Pluralizer {

    private Pluralizer() { }

    /**
     * Select numeral text
     *
     * @param value число
     * @param str1  First form
     * @param str2  Second form
     * @param str3  Third form
     * @return One from selected form
     */
    public static String evaluate(int value, String str1, String str2, String str3) {
        return evaluate((long) value, str1, str2, str3);
    }

    public static String evaluate(long value, String str1, String str2, String str3) {
        if ((value % 10 == 1) && (value % 100 != 11)) {
            return str1;
        } else if ((value % 10 >= 2) && (value % 10 <= 4) && (value % 100 < 10 || value % 100 >= 20)) {
            return str2;
        }
        return str3;
    }
}
