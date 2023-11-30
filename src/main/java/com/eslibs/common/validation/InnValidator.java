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

package com.eslibs.common.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InnValidator {

    private static final int[] N10_WEIGHTS = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] N11_WEIGHTS = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] N12_WEIGHTS = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    /**
     * Validate INN
     *
     * @param value string with INN
     * @return true if inn is valid or null
     */
    public boolean isValid(String value) {
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

    private boolean isValid12(String value) {
        return (Validators.asInt(value, 10) == getControlNumber(value, N11_WEIGHTS))
               && (Validators.asInt(value, 11) == getControlNumber(value, N12_WEIGHTS));
    }

    private boolean isValid10(String value) {
        return Validators.asInt(value, 9) == getControlNumber(value, N10_WEIGHTS);
    }

    private int getControlNumber(String value, int[] weights) {
        int res = 0;
        for (int i = 0; i < weights.length; ++i) {
            res += Validators.asInt(value, i) * weights[i];
        }
        return (res % 11) % 10;
    }

    public static InnValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static InnValidator INSTANCE = new InnValidator();
    }
}
