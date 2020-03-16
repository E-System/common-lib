package com.es.lib.common.number;

import com.es.lib.common.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NumberFormatter {

    private final int minDecimalCount;
    private final int maxDecimalCount;
    private final boolean chopZeroes;
    private final String decimalSymbol;
    private final Integer groupingSize;

    public String format(short value) {
        return format(value * 1.0d);
    }

    public String format(int value) {
        return format(value * 1.0d);
    }

    public String format(long value) {
        return format(value * 1.0f);
    }

    public String format(float value) {
        return format(value * 1.0d);
    }

    public String format(double value) {
        return chop(createFormat().format(value));
    }

    /**
     * Divide by 100.0d before format
     *
     * @param value Money count in pennies
     * @return Money in roubles
     */
    public String money(short value) {
        return money(value * 1.0d);
    }

    /**
     * Divide by 100.0d before format
     *
     * @param value Money count in pennies
     * @return Money in roubles
     */
    public String money(int value) {
        return money(value * 1.0d);
    }

    /**
     * Divide by 100.0d before format
     *
     * @param value Money count in pennies
     * @return Money in roubles
     */
    public String money(long value) {
        return money(value * 1.0d);
    }

    /**
     * Divide by 100.0d before format
     *
     * @param value Money count in pennies
     * @return Money in roubles
     */
    public String money(float value) {
        return money(value * 1.0d);
    }

    /**
     * Divide by 100.0d before format
     *
     * @param value Money count in pennies
     * @return Money in roubles
     */
    public String money(double value) {
        return format(value / 100.0d);
    }

    private DecimalFormat createFormat() {
        DecimalFormat result = new DecimalFormat();
        boolean groupingUsed = groupingSize != null && groupingSize > 0;
        result.setGroupingUsed(groupingUsed);
        result.setGroupingSize(groupingUsed ? groupingSize : Constant.DEFAULT_GROUPING_SIZE);
        result.setMinimumFractionDigits(minDecimalCount);
        result.setMaximumFractionDigits(maxDecimalCount);
        if (StringUtils.isNotBlank(decimalSymbol)) {
            DecimalFormatSymbols dfs = result.getDecimalFormatSymbols();
            dfs.setDecimalSeparator(decimalSymbol.charAt(0));
            result.setDecimalFormatSymbols(dfs);
        }
        return result;
    }

    private String chop(String value) {
        if (value == null || value.isEmpty() || minDecimalCount != maxDecimalCount) {
            return value;
        }
        if (chopZeroes) {
            return value.replace(decimalSymbol + StringUtils.repeat('0', maxDecimalCount), "");
        }
        return value;
    }
}
