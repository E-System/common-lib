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
package com.eslibs.common.model;

import com.eslibs.common.collection.Items;
import com.eslibs.common.text.Texts;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
public record FullName(String surname, String name, String patronymic, List<String> others) {

    public FullName(String fullName) {
        String surname = null;
        String name = null;
        String patronymic = null;
        List<String> others = null;
        if (StringUtils.isNotBlank(fullName)) {
            final String[] parts = Texts.splitBy("\\s+").toArray(fullName);
            if (parts.length >= 1) {
                surname = parts[0];
            }
            if (parts.length >= 2) {
                name = parts[1];
            }
            if (parts.length >= 3) {
                patronymic = parts[2];
            }
            if (parts.length >= 4) {
                others = Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length));
            }
        }
        this(surname, name, patronymic, others);
    }

    public FullName(String surname, String name, String patronymic) {
        this(surname, name, patronymic, null);
    }

    public String getPatronymicFull() {
        String result = Objects.toString(patronymic, "");
        if (Items.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public String getNotSurname() {
        String result = (StringUtils.isNotBlank(name) ? name : "")
                        + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
        if (Items.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public List<String> toList() {
        List<String> result = new ArrayList<>();
        if (surname != null) {
            result.add(surname);
        }
        if (name != null) {
            result.add(name);
        }
        if (patronymic != null) {
            result.add(patronymic);
        }
        if (others != null) {
            result.addAll(others);
        }
        return result;
    }

    public String[] toArray() {
        return toList().toArray(new String[0]);
    }

    public String getFull() {
        String result = (StringUtils.isNotBlank(surname) ? surname + " " : "")
                        + (StringUtils.isNotBlank(name) ? name : "")
                        + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
        if (Items.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public String getInitialsRight() {
        return initiator().get(this);
    }

    public String getInitialsLeft() {
        return initiator(true).get(this);
    }

    public static Initiator initiator() {
        return initiator(false);
    }

    public static Initiator initiator(boolean left) {
        return new Initiator(left);
    }

    public boolean isAllBlank() {
        return StringUtils.isBlank(surname)
               && StringUtils.isBlank(name)
               && StringUtils.isBlank(patronymic);
    }

    public boolean isAllEmpty() {
        return StringUtils.isEmpty(surname)
               && StringUtils.isEmpty(name)
               && StringUtils.isEmpty(patronymic);
    }

    public record Initiator(boolean left) {

        public String get(FullName fullName) {
            if (fullName == null || fullName.isAllBlank()) {
                return "";
            }
            final List<String> parts = fullName.toList();
            if (parts.isEmpty()) {
                return "";
            }
            return processList(parts);
        }

        public String get(String fullName) {
            if (StringUtils.isBlank(fullName)) {
                return "";
            }
            List<String> parts = Texts.splitBy("\\s+").toList(fullName);
            if (parts.isEmpty()) {
                return fullName;
            }
            return processList(parts);
        }


        private String processList(List<String> parts) {
            if (parts.size() == 1) {
                return parts.getFirst().trim();
            }
            String result = left ? "" : parts.getFirst().trim() + " ";
            StringJoiner joiner = new StringJoiner(" ");
            for (String p : parts.subList(1, parts.size())) {
                if (StringUtils.isNotBlank(p)) {
                    joiner.add(p.trim().charAt(0) + ".");
                }
            }
            result += joiner.toString();
            if (left) {
                result += (" " + parts.getFirst());
            }
            return result;
        }
    }
}
