package com.es.lib.common.validation;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
public final class OgrnValidatorUtil {

    private OgrnValidatorUtil() { }

    /**
     * General validate OGRN with 13 and 15 length
     *
     * @param value string with OGRN
     */
    public static boolean isValid(String value, OgrnType type) {
        if (value == null) {
            return true;
        }
        int len = value.length();
        if ((type != OgrnType.ANY && len != type.getValue()) || (type == OgrnType.ANY && len != OgrnType.OGRN.getValue() && len != OgrnType.OGRNIP.getValue())) {
            return false;
        }
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (len == 13) {
            return isValid13(value);
        } else {
            return isValid15(value);
        }
    }

    /**
     * Validate OGRN with length equal 13
     *
     * @param value string with OGRN
     */
    private static boolean isValid13(String value) {
        try {
            long num12 = (long) Math.floor((Long.parseLong(value) / 10) % 11);
            long dgt13 = num12 == 10 ? 0 : num12;
            if (ValidationUtil.getInt(value, 12) != dgt13) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Validate OGRN with length equal 15
     *
     * @param value string with OGRN
     */
    private static boolean isValid15(String value) {
        try {
            long num14 = (long) Math.floor((Long.parseLong(value) / 10) % 13);
            long dgt15 = num14 % 10;
            if (ValidationUtil.getInt(value, 14) != dgt15) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
