package com.es.lib.common.validation.bik;

import com.es.lib.common.validation.BadLengthException;
import com.es.lib.common.validation.BadValueException;

public class BikValidatorUtil {

    public static void validate(String value) throws BadValueException, BadLengthException {
        if (value == null || value.length() != 9) {
            throw new BadLengthException();
        }
        if (!value.matches("\\d{9}")) {
            throw new BadValueException();
        }
        int val = Integer.parseInt(value.substring(0, 2));
        if (val != 4) {
            throw new BadValueException();
        }
        val = Integer.parseInt(value.substring(6));
        if (val < 50) {
            throw new BadValueException();
        }
    }
}
