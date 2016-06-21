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

package com.es.lib.common;

import com.es.lib.common.exception.ESRuntimeException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class NumberUtil {

	public static final int SCALE = 6;
	public static final long MULTIPLIER = 1000000;

	private NumberUtil() {
	}

	public static BigDecimal percent(double val1, double val2) {
		return BigDecimal.valueOf((val1 / val2) * 100);
	}

	public static BigDecimal percent(BigDecimal val1, BigDecimal val2) {
		if (val1 == null || val2 == null) {
			throw new ESRuntimeException("ОШИБКА ВЫЧИСЛЕНИЯ ПРОЦЕНТА: [val1: " + val1 + ", val2: " + val2 + "]");
		}
		return percent(val1.doubleValue(), val2.doubleValue());
	}

	public static BigDecimal percent(int val1, int val2) {
		return percent(val1 * 1.0d, val2 * 1.0d);
	}

	public static long toScaledLong(Number value) {
		return value != null ? Math.round(value.doubleValue() * MULTIPLIER) : 0;
	}

	public static BigDecimal toBigDecimal(long value) {
		return value != 0 ? BigDecimal.valueOf(value, SCALE) : null;
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
		return BigDecimal.valueOf(res).setScale(7, RoundingMode.FLOOR).doubleValue();
	}

	public static BigDecimal remain(BigDecimal value, BigDecimal mult) {
		return toBigDecimal(remain(toScaledLong(value), toScaledLong(mult)));
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
				setScale(7, RoundingMode.FLOOR).doubleValue();
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
		return additionAndMult(toScaledLong(value), toScaledLong(mult));
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
			this.addition = toScaledLong(addition);
			this.total = toScaledLong(total);
		}

		public AAM(long addition, long total) {
			this.addition = addition;
			this.total = total;
		}

		public long getAddition() {
			return addition;
		}

		public BigDecimal getRealAddition() {
			return toBigDecimal(addition);
		}

		public long getTotal() {
			return total;
		}

		public BigDecimal getRealTotal() {
			return toBigDecimal(total);
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
