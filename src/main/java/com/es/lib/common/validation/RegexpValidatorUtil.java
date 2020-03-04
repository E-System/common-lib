package com.es.lib.common.validation;

import org.apache.commons.lang3.StringUtils;

public final class RegexpValidatorUtil {

    private RegexpValidatorUtil() { }

    public static boolean isValid(String value, String pattern) {
        if (value == null || StringUtils.isBlank(pattern)) {
            return true;
        }
        return value.matches(pattern);
    }
}
