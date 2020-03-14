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

package com.es.lib.common.number;

import com.es.lib.common.Constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class Numbers {

    private Numbers() { }

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
        return new NumberFormatter(decimalCount, chopZeroes, decimalSymbol, groupingSize);
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
        return BigDecimal.valueOf(res).setScale(Constant.PRECISED_SCALE, RoundingMode.FLOOR).doubleValue();
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
            setScale(Constant.PRECISED_SCALE, RoundingMode.FLOOR).doubleValue();
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

    public static class AAM {

        private long addition;
        private long total;

        public AAM(Double addition, Double total) {
            this.addition = converter(addition).asScaledLong();
            this.total = converter(total).asScaledLong();
        }

        public AAM(long addition, long total) {
            this.addition = addition;
            this.total = total;
        }

        public long getAddition() {
            return addition;
        }

        public BigDecimal getRealAddition() {
            return converter(addition).toBigDecimal();
        }

        public long getTotal() {
            return total;
        }

        public BigDecimal getRealTotal() {
            return converter(total).toBigDecimal();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            AAM aam = (AAM) o;

            return addition == aam.addition && total == aam.total;

        }

        @Override
        public int hashCode() {
            int result = (int) (addition ^ (addition >>> 32));
            result = 31 * result + (int) (total ^ (total >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "AAM [" +
                   "addition=" + addition +
                   ", total=" + total +
                   ']';
        }
    }
}
