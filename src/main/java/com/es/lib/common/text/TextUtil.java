package com.es.lib.common.text;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
public class TextUtil {

    public static Splitter splitBy(String regexp) {
        return splitBy(regexp, null, true);
    }

    public static Splitter splitBy(String regexp, boolean trim) {
        return splitBy(regexp, null, trim);
    }

    public static Splitter splitBy(String regexp, Integer limit) {
        return splitBy(regexp, limit, true);
    }

    public static Splitter splitBy(String regexp, Integer limit, boolean trim) {
        return new Splitter(regexp, limit, trim);
    }

    public static List<String> splitAsList(String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.asList(splitAsArray(value));
    }

    public static String[] splitAsArray(String value) {
        return value.trim().split("\\s+");
    }

    public static boolean contains(String value, String term) {
        return contains(value, term, true);
    }

    public static boolean contains(String value, String term, boolean ignoreCase) {
        if (value == null || term == null) {
            return false;
        }
        if (ignoreCase) {
            value = value.toLowerCase();
            term = term.toLowerCase();
        }
        return value.contains(term);
    }
}
