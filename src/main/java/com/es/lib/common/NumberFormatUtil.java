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

import com.es.lib.common.builder.DecimalFormatBuilder;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class NumberFormatUtil {

    private NumberFormatUtil() { }

    private static DecimalFormat getFormat03() {
        return new DecimalFormatBuilder().
            groupingUsed(false).
            fractionDigits(0, 3).
            decimalSymbol(',').build();
    }

    private static DecimalFormat getFormat00() {
        return new DecimalFormatBuilder().
            groupingUsed(false).
            fractionDigits(0).
            decimalSymbol(',').build();
    }

    public static String f03(Number value) {
        if (value == null) {
            return "NULL";
        }
        return getFormat03().format(value);
    }

    public static Number p03(String value) throws ParseException {
        return getFormat03().parse(value);
    }

    public static String f22(double price) {
        return f22(price, ',', null);
    }

    public static String f22(int price) {
        return f22(price / 100.0d);
    }

    public static String f22(long price) {
        return f22(price / 100.0d);
    }

    public static String f22(double price, char decimalSymbol) {
        return new DecimalFormatBuilder()
            .groupingUsed(false)
            .fractionDigits(2)
            .decimalSymbol(decimalSymbol).build()
            .format(price);
    }

    public static String f22(int price, char decimalSymbol) {
        return f22(price / 100.0d, decimalSymbol);
    }

    public static String f22(long price, char decimalSymbol) {
        return f22(price / 100.0d, decimalSymbol);
    }

    public static String f22(double price, char decimalSymbol, Integer groupingSize) {
        return new DecimalFormatBuilder()
            .groupingSize(groupingSize)
            .fractionDigits(2)
            .decimalSymbol(decimalSymbol).build()
            .format(price);
    }

    public static String f22(int price, char decimalSymbol, Integer groupingSize) {
        return f22(price / 100.0d, decimalSymbol, groupingSize);
    }

    public static String f22(long price, char decimalSymbol, Integer groupingSize) {
        return f22(price / 100.0d, decimalSymbol, groupingSize);
    }

    public static String f22(double price, String decimalSymbol) {
        return new DecimalFormatBuilder()
            .groupingUsed(false)
            .fractionDigits(2)
            .decimalSymbol(decimalSymbol).build()
            .format(price);
    }

    public static String f22(int price, String decimalSymbol) {
        return f22(price / 100.0d, decimalSymbol);
    }

    public static String f22(long price, String decimalSymbol) {
        return f22(price / 100.0d, decimalSymbol);
    }

    public static String f22(double price, String decimalSymbol, Integer groupingSize) {
        return new DecimalFormatBuilder()
            .groupingSize(groupingSize)
            .fractionDigits(2)
            .decimalSymbol(decimalSymbol).build()
            .format(price);
    }

    public static String f22(int price, String decimalSymbol, Integer groupingSize) {
        return f22(price / 100.0d, decimalSymbol, groupingSize);
    }

    public static String f22(long price, String decimalSymbol, Integer groupingSize) {
        return f22(price / 100.0d, decimalSymbol, groupingSize);
    }

    public static String f00(Number percent) {
        if (percent == null) {
            return "NULL";
        }
        return getFormat00().format(percent);
    }

    public static Number p00(String value) throws ParseException {
        return getFormat00().parse(value);
    }
}
