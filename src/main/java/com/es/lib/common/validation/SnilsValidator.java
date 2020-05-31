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
final class SnilsValidator {

    private SnilsValidator() { }

    /**
     * Validate SNILS
     *
     * @param value string with SNILS
     */
    public boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 11) {
            return false;
        }
        return getControlNumber(value).equals(value.substring(9, 11));
    }

    private String getControlNumber(String value) {
        int res = 0;
        for (int i = 0; i < 9; ++i) {
            res += Validators.asInt(value, i) * (9 - i);
        }
        if (res > 101) {
            res = res % 101;
        }
        if (res >= 100) {
            res = 0;
        }
        return String.format("%02d", res);
    }

    public static SnilsValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static SnilsValidator INSTANCE = new SnilsValidator();
    }
}
