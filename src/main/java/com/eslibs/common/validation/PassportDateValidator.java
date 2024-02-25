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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PassportDateValidator {

    public boolean isValid(LocalDate passportDate, LocalDate birthDate) {
        return isValid(passportDate, birthDate, null);
    }

    public boolean isValid(LocalDate passportDate, LocalDate birthDate, LocalDate now) {
        if (passportDate == null || birthDate == null) {
            return true;
        }
        long fullYears = ChronoUnit.YEARS.between(birthDate, now != null ? now : LocalDate.now());

        long passportYears = ChronoUnit.YEARS.between(birthDate, passportDate);

        return isValidStep(fullYears, passportYears, 45)
               && isValidStep(fullYears, passportYears, 20)
               && isValidStep(fullYears, passportYears, 14);
    }

    private boolean isValidStep(long fullYears, long passportYears, long value) {
        return fullYears < value || passportYears >= value;
    }

    public static PassportDateValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static PassportDateValidator INSTANCE = new PassportDateValidator();
    }
}
