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

package com.es.lib.common.validation.passport;

import com.es.lib.common.validation.BadValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
public final class PassportDateValidatorUtil {

    private PassportDateValidatorUtil() {
    }

    public static void validate(Date passportDate, Date birthDate) throws BadValueException {
        validate(passportDate, birthDate, null);
    }

    public static void validate(Date passportDate, Date birthDate, LocalDate now) throws BadValueException {
        if (passportDate == null || birthDate == null) {
            return;
        }

        LocalDate ldBirth = LocalDateTime.ofInstant(birthDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        long fullYears = ChronoUnit.YEARS.between(ldBirth, now != null ? now : LocalDate.now());

        LocalDate ldPassport = LocalDateTime.ofInstant(passportDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
        long passportYears = ChronoUnit.YEARS.between(ldBirth, ldPassport);

        if (isInvalidStep(fullYears, passportYears, 45)
            || isInvalidStep(fullYears, passportYears, 20)
            || isInvalidStep(fullYears, passportYears, 14)) {
            throw new BadValueException();
        }
    }

    private static boolean isInvalidStep(long fullYears, long passportYears, long value) {
        return fullYears >= value && passportYears < value;
    }
}
