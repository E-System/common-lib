/*
 * Copyright 2019 E-System LLC
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

package com.es.lib.common.security;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public final class Passwords {

    public static final int DEFAULT_LENGTH = 6;

    private Passwords() { }

    public static String numeric() {
        return numeric(DEFAULT_LENGTH);
    }

    public static String numeric(int length) {
        return generate(length, RandPass.NUMBERS_ALPHABET);
    }

    public static String alphaNumeric() {
        return alphaNumeric(false);
    }

    public static String alphaNumeric(boolean caps) {
        return alphaNumeric(DEFAULT_LENGTH, caps);
    }

    public static String alphaNumeric(int length, boolean caps) {
        return generate(length, caps ? RandPass.NUMBERS_AND_LETTERS_ALPHABET : RandPass.LOWERCASE_LETTERS_AND_NUMBERS_ALPHABET);
    }

    public static String generate(int length, char[] alphabet) {
        return new RandPass(alphabet).getPass(length);
    }

    public static Collection<StrengthFeature> checkStrength(String password, int minLength, boolean checkBlank, boolean checkLetter, boolean checkDigit) {
        Collection<StrengthFeature> result = new ArrayList<>(4);
        if (password == null) {
            password = "";
        }
        boolean blank = StringUtils.isBlank(password);
        if (checkBlank && blank) {
            result.add(StrengthFeature.Blank);
        }
        if (password.length() < minLength) {
            result.add(StrengthFeature.Length);
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char x = password.charAt(i);
            if (Character.isLetter(x)) {
                hasLetter = true;
            } else if (Character.isDigit(x)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                break;
            }
        }

        if (checkLetter && !hasLetter) {
            result.add(StrengthFeature.Letter);
        }
        if (checkDigit && !hasDigit) {
            result.add(StrengthFeature.Digit);
        }
        return result;
    }

    public enum StrengthFeature {
        Blank,
        Length,
        Letter,
        Digit
    }
}
