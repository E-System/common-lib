package com.es.lib.common.validation;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
public final class KppValidatorUtil {

    private KppValidatorUtil() { }

    /**
     * Validate KPP
     *
     * @param value string with KPP
     */
    public static boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 9) {
            return false;
        }
        return value.matches("\\d{4}[\\dA-Z][\\dA-Z]\\d{3}");
    }
}
