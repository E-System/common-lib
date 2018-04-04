package com.es.lib.common;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 04.04.2018
 */
public final class TaxUtil {

    private TaxUtil() {}

    /**
     * Calculate exclude tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value + tax
     * @return Tax value or value + tax value
     */
    public static double exclude(double value, double percent, boolean onlyPercent) {
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
    public static long exclude(long value, double percent, boolean onlyPercent) {
        return Math.round(exclude((double) value, percent, onlyPercent));
    }

    /**
     * Calculate exclude tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value + tax
     * @return Tax value or value + tax value
     */
    public static int exclude(int value, double percent, boolean onlyPercent) {
        return (int) exclude((long) value, percent, onlyPercent);
    }

    /**
     * Calculate include tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value - tax
     * @return Tax value or value - tax value
     */
    public static double include(double value, double percent, boolean onlyPercent) {
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
    public static long include(long value, double percent, boolean onlyPercent) {
        return Math.round(include((double) value, percent, onlyPercent));
    }

    /**
     * Calculate include tax
     *
     * @param value       Value
     * @param percent     Tax percent
     * @param onlyPercent Calculate only percent, otherwise value - tax
     * @return Tax value or value - tax value
     */
    public static int include(int value, double percent, boolean onlyPercent) {
        return (int) include((long) value, percent, onlyPercent);
    }
}
