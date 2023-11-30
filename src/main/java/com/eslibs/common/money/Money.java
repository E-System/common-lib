package com.eslibs.common.money;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 04.04.2018
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Money {

    public static MoneyToStr formatFullText() {
        return formatFullText("RUB");
    }

    public static MoneyToStr format() {
        return format("RUB");
    }

    public static MoneyToStr formatFullText(String currency) {
        return formatFullText(currency, "RUS");
    }

    public static MoneyToStr format(String currency) {
        return format(currency, "RUS");
    }

    public static MoneyToStr formatFullText(String currency, String language) {
        return format(currency, language, false);
    }

    public static MoneyToStr format(String currency, String language) {
        return format(currency, language, true);
    }

    public static MoneyToStr format(String currency, String language, boolean penniesAsNumber) {
        return new MoneyToStr(
            MoneyToStr.Currency.valueOf(currency),
            MoneyToStr.Language.valueOf(language),
            penniesAsNumber ? MoneyToStr.Pennies.NUMBER : MoneyToStr.Pennies.TEXT
        );
    }

    /**
     * Calculate exclude tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value + tax
     * @return Tax value or value + tax value
     */
    public static double taxExclude(double value, double percent, boolean onlyPercent) {
        double result = value * percent / 100.0d;
        if (onlyPercent) {
            return result;
        }
        return value + result;
    }

    /**
     * Calculate exclude tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value + tax
     * @return Tax value or value + tax value
     */
    public static long taxExclude(long value, double percent, boolean onlyPercent) {
        return Math.round(taxExclude((double) value, percent, onlyPercent));
    }

    /**
     * Calculate exclude tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value + tax
     * @return Tax value or value + tax value
     */
    public static int taxExclude(int value, double percent, boolean onlyPercent) {
        return (int) taxExclude((long) value, percent, onlyPercent);
    }

    /**
     * Calculate include tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value - tax
     * @return Tax value or value - tax value
     */
    public static double taxInclude(double value, double percent, boolean onlyPercent) {
        double d = percent / 100.0d;
        double result = (value / (1.0d + d)) * d;
        if (onlyPercent) {
            return result;
        }
        return value - result;
    }

    /**
     * Calculate include tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value - tax
     * @return Tax value or value - tax value
     */
    public static long taxInclude(long value, double percent, boolean onlyPercent) {
        return Math.round(taxInclude((double) value, percent, onlyPercent));
    }

    /**
     * Calculate include tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value - tax
     * @return Tax value or value - tax value
     */
    public static int taxInclude(int value, double percent, boolean onlyPercent) {
        return (int) taxInclude((long) value, percent, onlyPercent);
    }
}
