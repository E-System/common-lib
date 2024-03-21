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

package com.eslibs.common.security;

import com.eslibs.common.collection.Items;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Passwords {

    public static final int DEFAULT_LENGTH = 6;

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

    public static Set<StrengthFeature> strength(String password, StrengthFeature... features) {
        Set<StrengthFeature> featureSet = new HashSet<>(Arrays.asList(features));
        Set<StrengthFeature> result = new HashSet<>(4);
        password = StringUtils.defaultString(password);

        BlankFeature blankFeature = Items.firstByClass(featureSet, BlankFeature.class).orElse(null);
        if (blankFeature != null && StringUtils.isBlank(password)) {
            result.add(blankFeature);
        }
        LengthFeature lengthFeature = Items.firstByClass(featureSet, LengthFeature.class).orElse(null);
        if (lengthFeature != null && password.length() < lengthFeature.min()) {
            result.add(lengthFeature);
        }
        LetterFeature letterFeature = Items.firstByClass(featureSet, LetterFeature.class).orElse(null);
        DigitFeature digitFeature = Items.firstByClass(featureSet, DigitFeature.class).orElse(null);
        if (letterFeature == null && digitFeature == null) {
            return result;
        }
        int letterCount = 0;
        int digitCount = 0;
        for (int i = 0; i < password.length(); ++i) {
            char x = password.charAt(i);
            if (Character.isLetter(x)) {
                ++letterCount;
            } else if (Character.isDigit(x)) {
                ++digitCount;
            }
        }
        if (letterFeature != null && letterCount < letterFeature.min()) {
            result.add(letterFeature);
        }
        if (digitFeature != null && digitCount < digitFeature.min()) {
            result.add(digitFeature);
        }
        return result;
    }

    public sealed interface StrengthFeature permits BlankFeature, DigitFeature, LengthFeature, LetterFeature {}

    public record BlankFeature() implements StrengthFeature {}

    public record LengthFeature(int min) implements StrengthFeature {}

    public record LetterFeature(int min) implements StrengthFeature {

        public LetterFeature() {
            this(1);
        }
    }

    public record DigitFeature(int min) implements StrengthFeature {

        public DigitFeature() {
            this(1);
        }
    }
}
