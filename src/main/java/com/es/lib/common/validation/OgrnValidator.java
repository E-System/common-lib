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
package com.es.lib.common.validation;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
final class OgrnValidator {

    private OgrnValidator() { }

    /**
     * General validate OGRN with 13 and 15 length
     *
     * @param value string with OGRN
     */
    public boolean isValid(String value, OgrnType type) {
        if (value == null) {
            return true;
        }
        int len = value.length();
        if ((type != OgrnType.ANY && len != type.getValue()) || (type == OgrnType.ANY && len != OgrnType.OGRN.getValue() && len != OgrnType.OGRNIP.getValue())) {
            return false;
        }
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (len == 13) {
            return isValid13(value);
        } else {
            return isValid15(value);
        }
    }

    /**
     * Validate OGRN with length equal 13
     *
     * @param value string with OGRN
     */
    private boolean isValid13(String value) {
        try {
            long num12 = (long) Math.floor((Long.parseLong(value) / 10) % 11);
            long dgt13 = num12 == 10 ? 0 : num12;
            if (Validators.asInt(value, 12) != dgt13) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Validate OGRN with length equal 15
     *
     * @param value string with OGRN
     */
    private boolean isValid15(String value) {
        try {
            long num14 = (long) Math.floor((Long.parseLong(value) / 10) % 13);
            long dgt15 = num14 % 10;
            if (Validators.asInt(value, 14) != dgt15) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static OgrnValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static OgrnValidator INSTANCE = new OgrnValidator();
    }
}
