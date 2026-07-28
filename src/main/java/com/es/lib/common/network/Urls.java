package com.es.lib.common.network;

import com.es.lib.common.collection.CollectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Urls {

    public static List<String> split(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        List<String> result = Stream.of(url.trim().split("\n"))
            .map(String::trim)
            .map(v -> v.endsWith("/") ? v : (v + "/"))
            .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }
        return result;
    }

    public static boolean isValidList(String value) {
        return Stream.of(value.trim().split("\n")).allMatch(Urls::isValid);
    }

    public static boolean isValid(String value) {
        return isValid(value, true);
    }

    public static boolean isValid(String value, boolean allowNull) {
        if (value == null) {
            return allowNull;
        }
        try {
            new URL(value);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
