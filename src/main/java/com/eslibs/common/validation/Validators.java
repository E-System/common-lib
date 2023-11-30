/*
 * Copyright 2020 E-System LLC
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
package com.eslibs.common.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validators {

    public static final BikValidator BIK = BikValidator.getInstance();

    public static final InnValidator INN = InnValidator.getInstance();

    public static final KppValidator KPP = KppValidator.getInstance();

    public static final OgrnValidator OGRN = OgrnValidator.getInstance();

    public static final PassportDateValidator PASSPORT_DATE = PassportDateValidator.getInstance();

    public static final RangeValidator RANGE = RangeValidator.getInstance();

    public static final RegexpValidator REGEXP = RegexpValidator.getInstance();

    public static final SnilsValidator SNILS = SnilsValidator.getInstance();


    /**
     * Get double from Object(use toString)
     *
     * @param value source object
     * @return parsed double
     */
    public static double asDouble(Object value) {
        return asDouble(value.toString());
    }

    /**
     * Check valid double value from Object
     *
     * @param value source object
     * @return true if source is valid double
     */
    public static boolean isValidDouble(Object value) {
        try {
            asDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Get double from string
     *
     * @param value source string
     * @return parsed double
     */
    public static double asDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    /**
     * Parse sum (use * 100) from string
     *
     * @param value source string
     * @return parsed sum Math.round(value * 100)
     */
    public static long asSum(String value) {
        return Math.round(asDouble(value) * 100);
    }

    /**
     * Get parsed long from Object(use toString)
     *
     * @param value source object
     * @return parsed long
     */
    public static long asLong(Object value) {
        return asLong(value.toString());
    }

    /**
     * Check valid long from Object
     *
     * @param value source object
     * @return true if source is valid long
     */
    public static boolean isValidLong(Object value) {
        try {
            asLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Get parsed long from string
     *
     * @param value source string
     * @return parsed long
     */
    public static long asLong(String value) {
        return Long.parseLong(value);
    }

    /**
     * Get int representation value character at index
     *
     * @param value string
     * @param index index of char
     * @return int representation
     */
    public static int asInt(String value, int index) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (index < 0 || value.length() < index) {
            throw new IllegalArgumentException();
        }
        return Character.getNumericValue(value.charAt(index));
    }
}
