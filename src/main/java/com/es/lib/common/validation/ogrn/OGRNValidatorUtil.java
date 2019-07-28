package com.es.lib.common.validation.ogrn;

import com.es.lib.common.validation.BadLengthException;
import com.es.lib.common.validation.BadValueException;
import com.es.lib.common.validation.ValidationUtil;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.07.16
 */
public class OGRNValidatorUtil {

    private OGRNValidatorUtil() {
    }

    /**
     * General validate OGRN with 13 and 15 length
     *
     * @param value string with OGRN
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    public static void validate(String value) throws BadLengthException, BadValueException {
        if (value == null) {
            throw new BadLengthException();
        }
        int length = value.length();
        if (length != 13 && length != 15) {
            throw new BadLengthException();
        }
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new BadValueException();
        }
        if (length == 13) {
            validate13(value, false);
        } else {
            validate15(value, false);
        }
    }

    /**
     * Validate OGRN with length equal 13
     *
     * @param value string with OGRN
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    public static void validate13(String value) throws BadLengthException, BadValueException {
        validate13(value, true);
    }

    /**
     * Validate OGRN with length equal 15
     *
     * @param value string with OGRN
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    public static void validate15(String value) throws BadLengthException, BadValueException {
        validate15(value, true);
    }

    /**
     * Validate OGRN with length equal 13
     *
     * @param value    string with OGRN
     * @param preCheck Execute common check
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    private static void validate13(String value, boolean preCheck) throws BadLengthException, BadValueException {
        if (preCheck) {
            if (value == null) {
                throw new BadLengthException();
            }
            int length = value.length();
            if (length != 13) {
                throw new BadLengthException();
            }
            try {
                Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new BadValueException();
            }
        }
        try {
            long num12 = (long) Math.floor((Long.valueOf(value) / 10) % 11);
            long dgt13 = num12 == 10 ? 0 : num12;
            if (ValidationUtil.getInt(value, 12) != dgt13) {
                throw new BadValueException();
            }
        } catch (NumberFormatException e) {
            throw new BadValueException();
        }
    }

    /**
     * Validate OGRN with length equal 15
     *
     * @param value    string with OGRN
     * @param preCheck Execute common check
     * @throws BadValueException  when invalid length
     * @throws BadLengthException when invalid value
     */
    private static void validate15(String value, boolean preCheck) throws BadLengthException, BadValueException {
        if (preCheck) {
            if (value == null) {
                throw new BadLengthException();
            }
            int length = value.length();
            if (length != 15) {
                throw new BadLengthException();
            }
            if (value.matches("\\D")) {
                throw new BadValueException();
            }
        }
        try {
            long num14 = (long) Math.floor((Long.valueOf(value) / 10) % 13);
            long dgt15 = num14 % 10;
            if (ValidationUtil.getInt(value, 14) != dgt15) {
                throw new BadValueException();
            }
        } catch (NumberFormatException e) {
            throw new BadValueException();
        }
    }
}
