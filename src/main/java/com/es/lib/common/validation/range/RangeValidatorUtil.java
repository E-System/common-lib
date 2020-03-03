package com.es.lib.common.validation.range;

import com.es.lib.common.validation.ValidateException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RangeValidatorUtil {

    private RangeValidatorUtil() { }

    public static void validate(String value, String ranges) throws ValidateException {
        if (value == null || StringUtils.isBlank(ranges)) {
            return;
        }
        try {
            double numValue = Double.parseDouble(value);

            Pattern r = Pattern.compile("([\\[\\(][^\\]^\\)]*[\\]\\)])");
            Matcher m = r.matcher(ranges);
            while (m.find()) {
                String range = m.group();
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
                            return;
                        }
                    } catch (Exception ignore) { }
                } else if (rItem.isEmpty()) {
                    try {
                        double rVal = Double.parseDouble(rItem);
                        if ((rightInclude && numValue <= rVal) || numValue < rVal) {
                            return;
                        }
                    } catch (Exception ignore) { }
                } else {
                    try {
                        double lVal = Double.parseDouble(lItem);
                        double rVal = Double.parseDouble(rItem);
                        if (((leftInclude && numValue >= lVal) || numValue > lVal) && (rightInclude && numValue <= rVal) || (!rightInclude && numValue < rVal)) {
                            return;
                        }
                    } catch (Exception ignore) { }
                }
            }
        } catch (Exception ignore) { }
        throw new ValidateException();
    }
}
