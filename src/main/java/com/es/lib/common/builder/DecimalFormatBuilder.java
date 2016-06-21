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

package com.es.lib.common.builder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class DecimalFormatBuilder {

	private DecimalFormat format;

	public DecimalFormatBuilder(String pattern) {
		format = new DecimalFormat(pattern);
	}

	public DecimalFormatBuilder() {
		format = new DecimalFormat();
	}

	public DecimalFormatBuilder groupingUsed(boolean groupingUsed) {
		format.setGroupingUsed(groupingUsed);
		return this;
	}

	private DecimalFormatBuilder maximumFractionDigits(int digits) {
		format.setMaximumFractionDigits(digits);
		return this;
	}

	private DecimalFormatBuilder minimumFractionDigits(int digits) {
		format.setMinimumFractionDigits(digits);
		return this;
	}

	public DecimalFormatBuilder fractionDigits(int digits) {
		return minimumFractionDigits(digits).
				maximumFractionDigits(digits);
	}

	public DecimalFormatBuilder fractionDigits(int min, int max) {
		return minimumFractionDigits(min).
				maximumFractionDigits(max);
	}

	public DecimalFormatBuilder decimalSymbol(char symbol) {
		DecimalFormatSymbols dfs = format.getDecimalFormatSymbols();
		dfs.setDecimalSeparator(symbol);
		format.setDecimalFormatSymbols(dfs);
		return this;
	}

	public DecimalFormat build() {
		return format;
	}
}
