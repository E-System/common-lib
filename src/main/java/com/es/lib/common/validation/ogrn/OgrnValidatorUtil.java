package com.es.lib.common.validation.ogrn;

import com.es.lib.common.validation.ValidateException;
import com.es.lib.common.validation.ValidationUtil;

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
     * @throws ValidateException when invalid length
     */
    public static void validate(String value, Type type) throws ValidateException {
        if (value == null) {
            return;
        }
        int len = value.length();
        if ((type != Type.ANY && len != type.value) || (type == Type.ANY && len != Type.OGRN.value && len != Type.OGRNIP.value)) {
            throw new ValidateException();
        }
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ValidateException();
        }
        if (len == 13) {
            validate13(value);
        } else {
            validate15(value);
        }
    }

    /**
     * Validate OGRN with length equal 13
     *
     * @param value string with OGRN
     * @throws ValidateException when invalid length
     */
    private static void validate13(String value) throws ValidateException {
        try {
            long num12 = (long) Math.floor((Long.parseLong(value) / 10) % 11);
            long dgt13 = num12 == 10 ? 0 : num12;
            if (ValidationUtil.getInt(value, 12) != dgt13) {
                throw new ValidateException();
            }
        } catch (NumberFormatException e) {
            throw new ValidateException();
        }
    }

    /**
     * Validate OGRN with length equal 15
     *
     * @param value string with OGRN
     * @throws ValidateException when invalid length
     */
    private static void validate15(String value) throws ValidateException {
        try {
            long num14 = (long) Math.floor((Long.parseLong(value) / 10) % 13);
            long dgt15 = num14 % 10;
            if (ValidationUtil.getInt(value, 14) != dgt15) {
                throw new ValidateException();
            }
        } catch (NumberFormatException e) {
            throw new ValidateException();
        }
    }

    public enum Type {
        ANY(null),
        OGRN(13),
        OGRNIP(15);
        private Integer value;

        Type(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
