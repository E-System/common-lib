package com.es.lib.common.network;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Urls {

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
