package com.es.lib.common.validation.kpp;

import com.es.lib.common.validation.BadLengthException;
import com.es.lib.common.validation.BadValueException;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
public final class KPPValidatorUtil {

    private KPPValidatorUtil() { }

    /**
     * Validate KPP
     *
     * @param value string with KPP
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    public static void validate(String value) throws BadLengthException, BadValueException {
        if (value == null) {
            return;
        }
        if (value.length() != 9) {
            throw new BadLengthException();
        }
        if (!value.matches("\\d{4}[\\dA-Z][\\dA-Z]\\d{3}")) {
            throw new BadValueException();
        }
    }
}
