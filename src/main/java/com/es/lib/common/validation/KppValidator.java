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
public final class KppValidator {

    private KppValidator() { }

    /**
     * Validate KPP
     *
     * @param value string with KPP
     */
    public boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 9) {
            return false;
        }
        return value.matches("\\d{4}[\\dA-Z][\\dA-Z]\\d{3}");
    }

    public static KppValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static KppValidator INSTANCE = new KppValidator();
    }
}
