package com.es.lib.common.model;

import com.es.lib.common.collection.CollectionUtil;
import com.es.lib.common.text.FioChopper;
import com.es.lib.common.text.TextUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
public class FullName {

    private String surname;
    private String name;
    private String patronymic;
    private List<String> others;

    public FullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return;
        }
        final String[] parts = TextUtil.splitAsArray(fullName);
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

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPatronymicFull() {
        String result = StringUtils.defaultString(getPatronymic(), "");
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public String getNotSurname() {
        String result = (StringUtils.isNotBlank(name) ? name : "")
                        + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public List<String> getOthers() {
        return others;
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
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    public String getChoppedLeft() {
        return FioChopper.leftFullName(this);
    }

    public String getChoppedRight() {
        return FioChopper.rightFullName(this);
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

    @Override
    public String toString() {
        return "FullName{" +
               "surname='" + surname + '\'' +
               ", name='" + name + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", others=" + others +
               '}';
    }
}
