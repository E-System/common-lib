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

package com.es.lib.common.text;

import com.es.lib.common.model.FullName;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 06.11.15
 */
public class FioChopper {

    private FioChopper() {
    }

    /**
     * Сокращение Ф.И.О в формат Фамилия И. О.
     *
     * @param fullName входная строка с Ф.И.О.
     * @return сокращенная форма
     */
    public static String rightFullName(FullName fullName) {
        return process(fullName, false);
    }

    /**
     * Сокращение Ф.И.О в формат И. О. Фамилия
     *
     * @param fullName входная строка с Ф.И.О.
     * @return сокращенная форма
     */
    public static String leftFullName(FullName fullName) {
        return process(fullName, true);
    }

    /**
     * Сокращение Ф.И.О в формат Фамилия И. О.
     *
     * @param fullName входная строка с Ф.И.О.
     * @return сокращенная форма
     */
    public static String right(String fullName) {
        return process(fullName, false);
    }

    /**
     * Сокращение Ф.И.О в формат И. О. Фамилия
     *
     * @param fullName входная строка с Ф.И.О.
     * @return сокращенная форма
     */
    public static String left(String fullName) {
        return process(fullName, true);
    }

    private static String process(FullName fullName, boolean left) {
        if (fullName == null) {
            return null;
        }
        return processList(fullName.toList(), left);
    }

    private static String process(String fullName, boolean left) {
        if (StringUtils.isBlank(fullName)) {
            return fullName;
        }
        List<String> parts = TextUtil.splitAsList(fullName);
        if (parts.isEmpty()) {
            return fullName;
        }
        return processList(parts, left);
    }

    private static String processList(List<String> parts, boolean left) {
        if (parts.size() == 1) {
            return parts.get(0);
        }
        String result = left ? "" : parts.get(0) + " ";
        StringJoiner joiner = new StringJoiner(" ");
        for (String p : parts.subList(1, parts.size())) {
            joiner.add(p.trim().substring(0, 1) + ".");
        }
        result += joiner.toString();
        if (left) {
            result += (" " + parts.get(0));
        }
        return result;
    }
}
