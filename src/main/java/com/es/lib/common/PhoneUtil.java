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

package com.es.lib.common;

import com.es.lib.common.collection.CollectionUtil;
import com.es.lib.common.text.TextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utils for phone numbers
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 11.08.15
 */
public final class PhoneUtil {

    private static final Function<String, Map.Entry<String, Boolean>> typeMapper = v -> Pair.of(v, isMobile(v));
    private static final Function<String, Map.Entry<String, Boolean>> cleanTypeMapper = v -> Pair.of(clean(v), isMobile(v));

    private PhoneUtil() { }

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
        return value != null && value.matches("^(7|8)?9\\d{7,}$");
    }

    /**
     * Split input on phone pairs(number and mobile phone flag)
     *
     * @param value Input string
     * @param clean Clean numbers
     * @return Phone pairs
     */
    public static Collection<Map.Entry<String, Boolean>> split(String value, boolean clean) {
        final Collection<String> phones = TextUtil.splitBy("(,|;)").toList(value);
        return phones.stream().map(clean ? cleanTypeMapper : typeMapper).collect(Collectors.toList());
    }

    /**
     * Join phone numbers by types
     *
     * @param values    Numbers array
     * @param delimiter Delimiter for phones
     * @return 1 element - simple numbers, 2 element - mobile numbers
     */
    public static Map.Entry<String, String> joinByType(Collection<Map.Entry<String, Boolean>> values, String delimiter) {
        if (CollectionUtil.isEmpty(values)) {
            return null;
        }
        return Pair.of(
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
