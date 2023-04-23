/*
 * Copyright 2020 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.eslibs.common.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RangeValidator {

    private RangeValidator() { }

    public boolean isValid(String value, String ranges) {
        if (value == null || StringUtils.isBlank(ranges)) {
            return true;
        }
        try {
            double numValue = Double.parseDouble(value);

            Pattern r = Pattern.compile("([\\[(][^]^)]*[])])");
            Matcher m = r.matcher(ranges);
            while (m.find()) {
                String range = m.group();
                boolean leftInclude = range.startsWith("[");
                boolean rightInclude = range.endsWith("]");
                String[] items = range.replaceAll("[\\[]|[(]|[]]|[)]", "").split(";", 2);
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
                            return true;
                        }
                    } catch (Exception ignore) { }
                } else if (rItem.isEmpty()) {
                    try {
                        double rVal = Double.parseDouble(rItem);
                        if ((rightInclude && numValue <= rVal) || numValue < rVal) {
                            return true;
                        }
                    } catch (Exception ignore) { }
                } else {
                    try {
                        double lVal = Double.parseDouble(lItem);
                        double rVal = Double.parseDouble(rItem);
                        if (((leftInclude && numValue >= lVal) || numValue > lVal) && ((rightInclude && numValue <= rVal) || (!rightInclude && numValue < rVal))) {
                            return true;
                        }
                    } catch (Exception ignore) { }
                }
            }
        } catch (Exception ignore) { }
        return false;
    }

    public static RangeValidator getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    private static class InstanceWrapper {

        final static RangeValidator INSTANCE = new RangeValidator();
    }
}
