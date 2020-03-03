package com.es.lib.common.validation.kpp;

import com.es.lib.common.validation.ValidateException;

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
     * @throws ValidateException when invalid length
     */
    public static void validate(String value) throws ValidateException {
        if (value == null) {
            return;
        }
        if (value.length() != 9) {
            throw new ValidateException();
        }
        if (!value.matches("\\d{4}[\\dA-Z][\\dA-Z]\\d{3}")) {
            throw new ValidateException();
        }
    }
}
