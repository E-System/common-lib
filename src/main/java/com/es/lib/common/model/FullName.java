package com.es.lib.common.model;

import com.es.lib.common.collection.CollectionUtil;
import com.es.lib.common.text.FioChopper;
import com.es.lib.common.text.TextUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
@Getter
@ToString
@Builder
@Jacksonized
@EqualsAndHashCode
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FullName {

    private final String surname;
    private final String name;
    private final String patronymic;
    private final List<String> others;

    @Deprecated
    public FullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            surname = null;
            name = null;
            patronymic = null;
            others = null;
            return;
        }
        final String[] parts = TextUtil.splitAsArray(fullName);
        surname = parts.length >= 1 ? parts[0] : null;
        name = parts.length >= 2 ? parts[1] : null;
        patronymic = parts.length >= 3 ? parts[2] : null;
        others = parts.length >= 4 ? Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length)) : null;
    }

    public static FullName of(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return new FullName(null, null, null, null);
        }
        final String[] parts = TextUtil.splitAsArray(fullName);
        return new FullName(
            parts.length >= 1 ? parts[0] : null,
            parts.length >= 2 ? parts[1] : null,
            parts.length >= 3 ? parts[2] : null,
            parts.length >= 4 ? Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length)) : null
        );
    }

    public static FullName of(String surname, String name, String patronymic) {
        return new FullName(surname, name, patronymic, null);
    }


    @Deprecated
    public FullName(String surname, String name, String patronymic) {
        this(surname, name, patronymic, null);
    }

    @JsonIgnore
    public String getPatronymicFull() {
        String result = StringUtils.defaultString(getPatronymic(), "");
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    @JsonIgnore
    public String getNotSurname() {
        String result = (StringUtils.isNotBlank(name) ? name : "")
                        + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    @JsonIgnore
    public List<String> toList() {
        return Arrays.asList(toArray());
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getFull() {
        String result = (StringUtils.isNotBlank(surname) ? surname + " " : "")
                        + (StringUtils.isNotBlank(name) ? name : "")
                        + (StringUtils.isNotBlank(patronymic) ? " " + patronymic : "");
        if (CollectionUtil.isNotEmpty(others)) {
            result += (" " + String.join(" ", others));
        }
        return result.trim();
    }

    @JsonIgnore
    public String getChoppedLeft() {
        return FioChopper.leftFullName(this);
    }

    @JsonIgnore
    public String getChoppedRight() {
        return FioChopper.rightFullName(this);
    }

    @JsonIgnore
    public boolean isAllBlank() {
        return StringUtils.isBlank(surname)
               && StringUtils.isBlank(name)
               && StringUtils.isBlank(patronymic);
    }

    @JsonIgnore
    public boolean isAllEmpty() {
        return StringUtils.isEmpty(surname)
               && StringUtils.isEmpty(name)
               && StringUtils.isEmpty(patronymic);
    }
}
