package com.es.lib.common.network;

import com.es.lib.common.collection.CollectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Urls {

    public static String merge(List<String> items, String url) {
        List<String> result = new ArrayList<>(items != null ? items : Collections.emptyList());
        List<String> newUrls = split(url);
        if (newUrls != null) {
            result.addAll(0, newUrls);
        }
        return asString(result);
    }

    public static String asString(List<String> items) {
        if (CollectionUtil.isEmpty(items)) {
            return null;
        }
        return String.join("\n", items);
    }


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
            .map(v -> v + StringUtils.defaultString(path, ""))
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
