package com.es.lib.common.model;

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

    public List<String> toList() {
        return Arrays.asList(surname, name, patronymic);
    }

    public String[] toArray() {
        return new String[]{surname, name, patronymic};
    }

    public String getFull() {
        return (StringUtils.isNotBlank(surname) ? surname + " " : "")
               + (StringUtils.isNotBlank(name) ? name : "")
               + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
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
               '}';
    }
}
