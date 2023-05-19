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

import com.es.lib.common.collection.Items;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Calculate percent
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.11.15
 */
public final class Percents {

    private Percents() { }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(double part, double full) {
        if (full == 0.0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((part / full) * 100);
    }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(BigDecimal part, BigDecimal full) {
        if (part == null || full == null) {
            return BigDecimal.ZERO;
        }
        return value(part.doubleValue(), full.doubleValue());
    }

    /**
     * Get percent of part in full
     *
     * @param part Part sum
     * @param full Full sum
     * @return Percent of part in full (part / full)
     */
    public static BigDecimal value(int part, int full) {
        return value(part * 1.0d, full * 1.0d);
    }

    /**
     * Calculate int percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static int get(int value, double percent) {
        return (int) get((long) value, percent);
    }

    /**
     * Calculate long percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static long get(long value, double percent) {
        return Math.round(getDecimal(value, percent));
    }

    /**
     * Calculate double percent form value
     *
     * @param value   Value
     * @param percent Percent
     * @return Value percent
     */
    public static double getDecimal(double value, double percent) {
        return value * percent / 100.0d;
    }

    /**
     * Calculate int value with percent
     *
     * @param value   Value
     * @param percent Percent
     * @return Value + value percent
     */
    public static int getTotal(int value, double percent) {
        return value + get(value, percent);
    }

    /**
     * Calculate long value with percent
     *
     * @param value   Value
     * @param percent Percent
     * @return Value + value percent
     */
    public static long getTotal(long value, double percent) {
        return value + get(value, percent);
    }

    /**
     * Calculate int value with percent discount
     *
     * @param value   Value
     * @param percent Percent
     * @return Value - value percent
     */
    public static int withDiscount(int value, double percent) {
        return value - get(value, percent);
    }

    /**
     * Calculate long value with percent discount
     *
     * @param value   Value
     * @param percent Percent
     * @return Value - value percent
     */
    public static long withDiscount(long value, double percent) {
        return value - get(value, percent);
    }

    /**
     * Split items sum by percent sum in fullSum
     *
     * @param sum        Sum part
     * @param fullSum    Full sum
     * @param items      Items
     * @param sumFetcher Fetcher for sum from item
     * @param <T>        Type of items
     * @return List of items with calculated sum
     */
    public static <T> List<Map.Entry<T, Integer>> split(int sum, int fullSum, Collection<T> items, Function<T, Integer> sumFetcher) {
        if (Items.isEmpty(items)) {
            return new ArrayList<>();
        }
        double percent = 100.d;
        if (sum != fullSum) {
            percent = value(sum, fullSum).doubleValue();
        }
        int totalSum = 0;
        List<Map.Entry<T, Integer>> result = new ArrayList<>();
        for (T contractItem : items) {
            int sumPart = sumFetcher.apply(contractItem);
            if (sum != fullSum) {
                sumPart = get(sumPart, percent);
            }
            result.add(MutablePair.of(contractItem, sumPart));
            totalSum += sumPart;
        }
        if (totalSum != sum) {
            Map.Entry<T, Integer> entry = result.get(0);
            entry.setValue(entry.getValue() + (sum - totalSum));
        }
        return result;
    }

    /**
     * Split sum on percent parts
     *
     * @param sum         Input sum (Need be positive)
     * @param percents    List of percents
     * @param adjustFirst Add overlap to first element, otherwise to last
     * @return List of sum parts
     */
    public static List<Integer> split(int sum, Collection<Double> percents, boolean adjustFirst) {
        if (sum < 0) {
            throw new IllegalArgumentException("Sum must be positive");
        }
        if (percents == null) {
            throw new IllegalArgumentException("Percents must not be null");
        }
        List<Integer> result = new ArrayList<>(percents.size());
        int calculated = 0;
        for (double percent : percents) {
            int value = get(sum, percent);
            result.add(value);
            calculated += value;
        }
        if (calculated != sum) {
            int index = adjustFirst ? 0 : result.size() - 1;
            result.set(index, result.get(index) + (sum - calculated));
        }
        return result;
    }
}
