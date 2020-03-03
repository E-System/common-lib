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

package com.es.lib.common.validation.inn;

import com.es.lib.common.validation.ValidateException;
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
     * @throws ValidateException when invalid length
     */
    public static void validate(String value) throws ValidateException {
        if (value == null) {
            return;
        }
        int len = value.length();
        if (len != 10 && len != 12) {
            throw new ValidateException();
        }
        if (len == 10) {
            validate10(value);
        } else {
            validate12(value);
        }
    }

    private static void validate12(String value) throws ValidateException {
        if ((ValidationUtil.getInt(value, 10) != getControlNumber(value, N11_WEIGHTS))
            || (ValidationUtil.getInt(value, 11) != getControlNumber(value, N12_WEIGHTS))) {
            throw new ValidateException();
        }
    }

    private static void validate10(String value) throws ValidateException {
        if (ValidationUtil.getInt(value, 9) != getControlNumber(value, N10_WEIGHTS)) {
            throw new ValidateException();
        }
    }

    private static int getControlNumber(String value, int[] weights) {
        int res = 0;
        for (int i = 0; i < weights.length; ++i) {
            res += ValidationUtil.getInt(value, i) * weights[i];
        }
        return (res % 11) % 10;
    }
}
