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

    public static List<String> splitAsList(String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }
        return Arrays.asList(splitAsArray(value));
    }

    public static String[] splitAsArray(String value) {
        return value.trim().split("\\s+");
    }
}
