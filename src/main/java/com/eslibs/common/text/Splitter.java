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

package com.eslibs.common.text;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Splitter {

    private final String regexp;
    private final Integer limit;
    private final boolean trim;

    public String[] toArray(String text) {
        return toList(text).toArray(new String[]{});
    }

    public List<String> toList(String text) {
        if (StringUtils.isEmpty(text)) {
            return Collections.emptyList();
        }
        final String[] parts = internalSplit(text);
        if (!trim) {
            return Arrays.asList(parts);
        }
        return Stream.of(parts).map(String::trim).toList();
    }

    public <T> T toObject(String text, Function<String[], T> convert) {
        String[] split = toArray(text);
        if (split.length == 0) {
            return null;
        }
        try {
            return convert.apply(split);
        } catch (Exception e) {
            return null;
        }
    }

    protected String[] internalSplit(String text) {
        return limit != null ? text.split(regexp, limit) : text.split(regexp);
    }

    protected String trim(String text) {
        return trim ? text.trim() : text;
    }

    public SecondSplitter splitBy(String regexp) {
        return new SecondSplitter(this, regexp, null, true);
    }

    public SecondSplitter splitBy(String regexp, boolean trim) {
        return new SecondSplitter(this, regexp, null, trim);
    }

    public SecondSplitter splitBy(String regexp, Integer limit) {
        return new SecondSplitter(this, regexp, limit, true);
    }

    public SecondSplitter splitBy(String regexp, Integer limit, boolean trim) {
        return new SecondSplitter(this, regexp, limit, trim);
    }


    @Getter
    @ToString(callSuper = true)
    public static class SecondSplitter extends Splitter {

        private final Splitter firstSplitter;

        public SecondSplitter(Splitter firstSplitter, String regexp, Integer limit, boolean trim) {
            super(regexp, limit, trim);
            this.firstSplitter = firstSplitter;
        }

        public Map<String, String> toMap(String text) {
            if (StringUtils.isEmpty(text)) {
                return Collections.emptyMap();
            }
            return toMap(text, new LinkedHashMap<>());
        }

        public Map<String, String> toMap(String text, Map<String, String> result) {
            for (String row : firstSplitter.internalSplit(text)) {
                String[] parts = internalSplit(firstSplitter.trim(row));
                String key = null;
                String value = null;
                if (parts.length == 2) {
                    key = trim(parts[0]);
                    value = trim(parts[1]);
                }
                if (parts.length == 1) {
                    key = trim(parts[0]);
                }
                if (key != null) {
                    result.put(key, value);
                }
            }
            return result;
        }
    }
}

