package com.es.lib.common.network;

import com.es.lib.common.collection.CollectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Urls {

    public static String withSlash(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return url.endsWith("/") ? url : url + "/";
    }

    public static List<String> split(String url) {
        return split(url, null);
    }

    public static List<String> split(String url, String path) {
        if (StringUtils.isBlank(url)) {
            return new ArrayList<>();
        }
        List<String> result = toStream(url)
            .map(Urls::withSlash)
            .map(v -> path != null ? withSlash(v + path) : v)
            .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }
        return result;
    }

    public static boolean isValidList(String value) {
        return toStream(value).allMatch(Urls::isValid);
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

    private static Stream<String> toStream(String value) {
        if (StringUtils.isBlank(value)) {
            return Stream.empty();
        }
        return Stream.of(value.trim().split("\n")).map(String::trim);
    }
}
