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

package com.eslibs.common;

import com.eslibs.common.collection.Items;
import com.eslibs.common.text.Texts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Phone numbers
 *
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 11.08.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Phones {

    private static final Function<String, Map.Entry<String, Boolean>> typeMapper = v -> Map.entry(v, isMobile(v));
    private static final Function<String, Map.Entry<String, Boolean>> cleanTypeMapper = v -> Map.entry(clean(v), isMobile(v));

    /**
     * Clean phone number (remove spaces, braces, dashes...)
     *
     * @param value Phone number
     * @return Cleaned phone number
     */
    public static String clean(String value) {
        if (value == null) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }

    /**
     * Check phone number for mobile(7, 8 or blank in begin, next 9 and 7 digits)
     *
     * @param value Phone number
     * @return true - is mobile number
     */
    public static boolean isMobile(String value) {
        value = clean(value);
        return value != null && value.matches("^([78])?9\\d{7,}$");
    }

    /**
     * Split input on phone pairs(number and mobile phone flag)
     *
     * @param value Input string
     * @param clean Clean numbers
     * @return Phone pairs
     */
    public static Collection<Map.Entry<String, Boolean>> split(String value, boolean clean) {
        final Collection<String> phones = Texts.splitBy("(,|;)").toList(value);
        return phones.stream().map(clean ? cleanTypeMapper : typeMapper).toList();
    }

    /**
     * Join phone numbers by types
     *
     * @param values    Numbers array
     * @param delimiter Delimiter for phones
     * @return 1 element - simple numbers, 2 element - mobile numbers
     */
    public static Map.Entry<String, String> joinByType(Collection<Map.Entry<String, Boolean>> values, String delimiter) {
        if (Items.isEmpty(values)) {
            return null;
        }
        return Map.entry(
            values.stream().filter(v -> !v.getValue()).map(Map.Entry::getKey).collect(Collectors.joining(delimiter)),
            values.stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.joining(delimiter))
        );
    }

    /**
     * Convert input string to 2 string with typed numbers
     *
     * @param value     Input string
     * @param clean     Clean numbers in output string
     * @param delimiter Delimiter for numbers in output string
     * @return 1 element - simple numbers, 2 element - mobile numbers
     */
    public static Map.Entry<String, String> groupByType(String value, boolean clean, String delimiter) {
        return joinByType(
            split(value, clean),
            delimiter
        );
    }

    /**
     * Format phone number by mask
     *
     * @param value Input number
     * @param mask  Mask (for example +7 (***) ***-****)
     * @param full  Fill not available symbols in phone from mask
     * @return Formatted phone
     */
    public static String format(String value, String mask, boolean full) {
        if (value == null) {
            value = "";
        }
        if (StringUtils.isEmpty(mask)) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        for (; i < mask.length() && j < value.length(); ++i) {
            char maskChar = mask.charAt(i);
            sb.append(maskChar == '*' ? value.charAt(j++) : maskChar);
        }
        if (full) {
            for (; i < mask.length(); ++i) {
                char maskChar = mask.charAt(i);
                sb.append(maskChar == '*' ? ' ' : maskChar);
            }
        }
        return sb.toString();
    }
}
