package com.es.lib.common.validation.snils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SnilsHelper {

    public static boolean isValid(String value, boolean allowEmpty) {
        String cleared = clear(value);
        if (allowEmpty && cleared == null) {
            return true;
        }
        try {
            SNILSValidatorUtil.validate(cleared);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String clear(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("\\D", "");
    }

    public static String format(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1-$2-$3 $4");
    }
}