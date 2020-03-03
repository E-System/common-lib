package com.es.lib.common.validation.bik;

import com.es.lib.common.validation.ValidateException;

public class BikValidatorUtil {

    public static void validate(String value) throws ValidateException {
        if (value == null) {
            return;
        }
        if (value.length() != 9) {
            throw new ValidateException();
        }
        if (!value.matches("\\d{9}")) {
            throw new ValidateException();
        }
        int val = Integer.parseInt(value.substring(0, 2));
        if (val != 4) {
            throw new ValidateException();
        }
        val = Integer.parseInt(value.substring(6));
        if (val < 50) {
            throw new ValidateException();
        }
    }
}
