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

import com.es.lib.common.validation.ValidationUtil;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
public final class InnValidatorUtil {

    private static int[] N10_WEIGHTS = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static int[] N11_WEIGHTS = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static int[] N12_WEIGHTS = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    private InnValidatorUtil() { }

    /**
     * Validate INN
     *
     * @param value string with INN
     */
    public static boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        int len = value.length();
        if (len != 10 && len != 12) {
            return false;
        }
        if (len == 10) {
            return isValid10(value);
        } else {
            return isValid12(value);
        }
    }

    private static boolean isValid12(String value) {
        return (ValidationUtil.getInt(value, 10) == getControlNumber(value, N11_WEIGHTS))
               && (ValidationUtil.getInt(value, 11) == getControlNumber(value, N12_WEIGHTS));
    }

    private static boolean isValid10(String value) {
        return ValidationUtil.getInt(value, 9) == getControlNumber(value, N10_WEIGHTS);
    }

    private static int getControlNumber(String value, int[] weights) {
        int res = 0;
        for (int i = 0; i < weights.length; ++i) {
            res += ValidationUtil.getInt(value, i) * weights[i];
        }
        return (res % 11) % 10;
    }
}
