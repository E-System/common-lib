package com.es.lib.common.validation.regexp;

import com.es.lib.common.validation.ValidateException;
import org.apache.commons.lang3.StringUtils;

public final class RegexpValidatorUtil {

    private RegexpValidatorUtil() { }

    public static void validate(String value, String pattern) throws ValidateException {
        if (value == null || StringUtils.isBlank(pattern)) {
            return;
        }
        if (!value.matches(pattern)) {
            throw new ValidateException();
        }
    }
}
