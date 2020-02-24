package com.es.lib.common.validation.regexp;

import com.es.lib.common.validation.BadValueException;
import org.apache.commons.lang3.StringUtils;

public final class RegexpValidatorUtil {

    private RegexpValidatorUtil() { }

    public static void validate(String value, String pattern) throws BadValueException {
        if (StringUtils.isBlank(pattern)) {
            return;
        }
        if (value != null && !value.matches(pattern)) {
            throw new BadValueException();
        }
    }
}
