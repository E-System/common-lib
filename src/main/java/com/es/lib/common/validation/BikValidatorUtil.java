package com.es.lib.common.validation;

public class BikValidatorUtil {

    public static boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 9) {
            return false;
        }
        if (!value.matches("\\d{9}")) {
            return false;
        }
        int val = Integer.parseInt(value.substring(0, 2));
        if (val != 4) {
            return false;
        }
        val = Integer.parseInt(value.substring(6));
        return val >= 50;
    }
}
