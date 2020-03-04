package com.es.lib.common.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RangeValidatorUtil {

    private RangeValidatorUtil() { }

    public static boolean isValid(String value, String ranges) {
        if (value == null || StringUtils.isBlank(ranges)) {
            return true;
        }
        try {
            double numValue = Double.parseDouble(value);

            Pattern r = Pattern.compile("([\\[\\(][^\\]^\\)]*[\\]\\)])");
            Matcher m = r.matcher(ranges);
            while (m.find()) {
                String range = m.group();
                System.out.println("RANGE: " + range);
                boolean leftInclude = range.startsWith("[");
                boolean rightInclude = range.endsWith("]");
                String[] items = range.replaceAll("[\\[]|[\\(]|[\\]]|[\\)]", "").split(";", 2);
                if (items.length < 2) {
                    continue;
                }
                String lItem = items[0].trim();
                String rItem = items[1].trim();
                if (lItem.isEmpty() && rItem.isEmpty()) {
                    continue;
                }
                if (lItem.isEmpty()) {
                    try {
                        double lVal = Double.parseDouble(lItem);
                        if ((leftInclude && numValue >= lVal) || numValue > lVal) {
                            System.out.println("FIRST RETURN");
                            return true;
                        }
                    } catch (Exception ignore) { }
                } else if (rItem.isEmpty()) {
                    try {
                        double rVal = Double.parseDouble(rItem);
                        if ((rightInclude && numValue <= rVal) || numValue < rVal) {
                            System.out.println("SECOND RETURN");
                            return true;
                        }
                    } catch (Exception ignore) { }
                } else {
                    try {
                        double lVal = Double.parseDouble(lItem);
                        double rVal = Double.parseDouble(rItem);
                        if (((leftInclude && numValue >= lVal) || numValue > lVal) && ((rightInclude && numValue <= rVal) || (!rightInclude && numValue < rVal))) {
                            System.out.println("THIRD RETURN");
                            return true;
                        }
                    } catch (Exception ignore) { }
                }
            }
        } catch (Exception ignore) { }
        return false;
    }
}
