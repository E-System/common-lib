package com.es.lib.common.model;

import com.es.lib.common.collection.Items;
import com.es.lib.common.text.Texts;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
@Getter
@ToString
public class FullName {

    private String surname;
    private String name;
    private String patronymic;
    private List<String> others;

    public FullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return;
        }
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

    public FullName(String surname, String name, String patronymic) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }

    public String getPatronymicFull() {
        String result = StringUtils.defaultString(getPatronymic(), "");
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
        return Arrays.asList(toArray());
    }

    public String[] toArray() {
        if (surname != null) {
            if (name != null) {
                if (patronymic != null) {
                    return new String[]{surname, name, patronymic};
                }
                return new String[]{surname, name};
            }
            return new String[]{surname};
        }
        return new String[0];
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

    public static class Initiator {

        private boolean left;

        Initiator(boolean left) {
            this.left = left;
        }

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
                return parts.get(0).trim();
            }
            String result = left ? "" : parts.get(0).trim() + " ";
            StringJoiner joiner = new StringJoiner(" ");
            for (String p : parts.subList(1, parts.size())) {
                if (StringUtils.isNotBlank(p)) {
                    joiner.add(p.trim().substring(0, 1) + ".");
                }
            }
            result += joiner.toString();
            if (left) {
                result += (" " + parts.get(0));
            }
            return result;
        }
    }
}
