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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringSplitter {

    public static Collection<String> process(String text, Splitter splitter) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        final String[] parts = internalSplit(text, splitter);
        if (!splitter.isTrim()) {
            return Arrays.asList(parts);
        }
        return Stream.of(parts).map(String::trim).collect(Collectors.toList());
    }

    public static Collection<Map.Entry<String, String>> process(String text, Splitter first, Splitter second) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        return process(text, first, second, new LinkedList<>());
    }

    public static Collection<Map.Entry<String, String>> process(String text, Splitter first, Splitter second, Collection<Map.Entry<String, String>> result) {
        for (String row : internalSplit(text, first)) {
            String[] parts = internalSplit(trim(row, first), second);
            String key = null;
            String value = null;
            if (parts.length == 2) {
                key = trim(parts[0], second);
                value = trim(parts[1], second);
            }
            if (parts.length == 1) {
                key = trim(parts[0], second);
            }
            if (key != null) {
                result.add(Pair.of(key, value));
            }
        }
        return result;
    }

    private static String[] internalSplit(String text, Splitter splitter) {
        return splitter.getLimit() != null ? text.split(splitter.getRegexp(), splitter.getLimit()) : text.split(splitter.getRegexp());
    }

    private static String trim(String text, Splitter splitter) {
        return splitter.isTrim() ? text.trim() : text;
    }

    public static Splitter splitter(String regexp) {
        return new Splitter(regexp, null);
    }

    public static Splitter splitter(String regexp, boolean trim) {
        return new Splitter(regexp, null, trim);
    }

    public static Splitter splitter(String regexp, Integer limit) {
        return new Splitter(regexp, limit, true);
    }

    public static Splitter splitter(String regexp, Integer limit, boolean trim) {
        return new Splitter(regexp, limit, trim);
    }

    public static class Splitter {

        private final String regexp;
        private final Integer limit;
        private final boolean trim;

        Splitter(String regexp) {
            this(regexp, null);
        }

        Splitter(String regexp, boolean trim) {
            this(regexp, null, trim);
        }

        Splitter(String regexp, Integer limit) {
            this(regexp, limit, true);
        }

        Splitter(String regexp, Integer limit, boolean trim) {
            this.regexp = regexp;
            this.limit = limit;
            this.trim = trim;
        }

        public String getRegexp() {
            return regexp;
        }

        public Integer getLimit() {
            return limit;
        }

        public boolean isTrim() {
            return trim;
        }

        @Override
        public String toString() {
            return "Splitter{" +
                   "regexp='" + regexp + '\'' +
                   ", limit=" + limit +
                   ", trim=" + trim +
                   '}';
        }
    }
}

