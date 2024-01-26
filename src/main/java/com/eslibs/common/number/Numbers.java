/*
 * Copyright 2016 E-System LLC
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

package com.eslibs.common.number;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Numbers {

    /**
     * Split sum on count part
     *
     * @param sum         Input sum (Need be positive)
     * @param count       Split count
     * @param adjustFirst Add overlap to first element, otherwise to last
     * @return List of sum parts
     */
    public static List<Integer> splitSum(int sum, int count, boolean adjustFirst) {
        if (sum < 0) {
            throw new IllegalArgumentException("Sum must be positive");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than zero");
        }
        if (sum == 0) {
            return Collections.singletonList(0);
        }
        if (count == 1) {
            return Collections.singletonList(sum);
        }
        List<Integer> result = new ArrayList<>(count);
        int part = sum / count;
        int over = sum;
        int overlap = sum - (part * count);
        while (over > 0) {
            if (over >= part) {
                result.add(part);
            }
            over -= part;
        }
        if (overlap > 0) {
            int index = adjustFirst ? 0 : result.size() - 1;
            result.set(index, result.get(index) + overlap);
        }
        return result;
    }

    public static NumberConverter converter(Number value) {
        return new NumberConverter(value);
    }

    public static NumberParser parser(String value) {
        return new NumberParser(value);
    }

    public static NumberFormatter formatter() {
        return formatter(2, false);
    }

    public static NumberFormatter formatter(String decimalSymbol) {
        return formatter(2, false, decimalSymbol);
    }

    public static NumberFormatter formatter(int decimalCount, int groupingSize) {
        return formatter(decimalCount, false, ",", groupingSize);
    }

    public static NumberFormatter formatter(boolean chopZeroes) {
        return formatter(2, chopZeroes);
    }

    public static NumberFormatter formatter(int decimalCount, boolean chopZeroes) {
        return formatter(decimalCount, chopZeroes, ",");
    }

    public static NumberFormatter formatter(int decimalCount, boolean chopZeroes, String decimalSymbol) {
        return formatter(decimalCount, chopZeroes, decimalSymbol, 0);
    }

    public static NumberFormatter formatter(int decimalCount, boolean chopZeroes, String decimalSymbol, int groupingSize) {
        return formatter(decimalCount, decimalCount, chopZeroes, decimalSymbol, groupingSize);
    }

    public static NumberFormatter formatter(int minDecimalCount, int maxDecimalCount, boolean chopZeroes, String decimalSymbol, int groupingSize) {
        return formatter(minDecimalCount, maxDecimalCount, chopZeroes, decimalSymbol, groupingSize, null);
    }

    public static NumberFormatter formatter(int minDecimalCount, int maxDecimalCount, boolean chopZeroes, String decimalSymbol, int groupingSize, String groupingSymbol) {
        return new NumberFormatter(minDecimalCount, maxDecimalCount, chopZeroes, decimalSymbol, groupingSize, groupingSymbol);
    }

    public static double remain(double value, double mult, boolean scale) {
        double res;
        if (value < mult) {
            res = mult - value;
        } else {
            res = value - mult * Math.floor(value / mult);
        }
        if (!scale) {
            return res;
        }
        return BigDecimal.valueOf(res).setScale(NumberConverter.PRECISED_SCALE, RoundingMode.FLOOR).doubleValue();
    }

    public static BigDecimal remain(BigDecimal value, BigDecimal mult) {
        return converter(remain(converter(value).asScaledLong(), converter(mult).asScaledLong())).toBigDecimal();
    }

    public static long remain(long value, long mult) {
        return value % mult;
    }

    public static double multiplicity(double value, double mult) {
        double res;
        if (value < mult) {
            res = mult;
        } else {
            double remain = remain(value, mult, false);
            if (remain > 0.0d) {
                res = value + (mult - remain);
            } else {
                res = value;
            }
        }
        return BigDecimal.valueOf(res).
            setScale(NumberConverter.PRECISED_SCALE, RoundingMode.FLOOR).doubleValue();
    }

    public static long mult(long value, long mult) {
        return mult(value, mult, addition(value, mult));
    }

    public static long mult(long value, long mult, long add) {
        return value < mult ? mult : value + add;
    }

    public static long addition(long value, long mult) {
        long remain = remain(value, mult);
        if (remain == 0) {
            return 0;
        }
        return mult - remain;
    }

    public static AAM additionAndMult(BigDecimal value, BigDecimal mult) {
        return additionAndMult(converter(value).asScaledLong(), converter(mult).asScaledLong());
    }

    public static AAM additionAndMult(long value, long mult) {
        long addition = addition(value, mult);
        return new AAM(
            addition,
            mult(value, mult, addition)
        );
    }

    public enum Round {
        CEIL,
        FLOOR,
        MATH,
        BANK;

        public long get(long val, long vr) {
            return get(val, vr, this);
        }

        long get(long val, long vr, Round mode) {
            if (vr == 0) {
                return val;
            }
            long up = val % vr;
            if (up == 0) {
                return val;
            }
            long div = val / vr;
            switch (mode) {
                case MATH:
                case BANK: {
                    if (up >= vr / 2) {
                        return val + (vr - up);
                    } else {
                        return div * vr;
                    }
                }
                case CEIL:
                    return val + (vr - up);
                case FLOOR:
                    return div * vr;
                default:
                    return val;
            }
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class AAM {

        private final long addition;
        private final long total;

        public AAM(Double addition, Double total) {
            this.addition = converter(addition).asScaledLong();
            this.total = converter(total).asScaledLong();
        }

        public BigDecimal getRealAddition() {
            return converter(addition).toBigDecimal();
        }

        public BigDecimal getRealTotal() {
            return converter(total).toBigDecimal();
        }
    }
}
