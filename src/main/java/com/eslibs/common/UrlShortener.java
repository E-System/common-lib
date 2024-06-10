package com.eslibs.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlShortener {

    private static final String MAPPING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = MAPPING.length();

    public static String toUrl(long value) {
        StringBuilder builder = new StringBuilder();
        while (value > 0) {
            builder.append(MAPPING.charAt((int) (value % LENGTH)));
            value = value / LENGTH;
        }
        return builder.reverse().toString();
    }

    public static long toValue(String value) {
        long result = 0;

        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if ('a' <= c && c <= 'z')
                result = result * LENGTH + c - 'a';
            if ('A' <= c && c <= 'Z')
                result = result * LENGTH + c - 'A' + 26;
            if ('0' <= c && c <= '9')
                result = result * LENGTH + c - '0' + 52;
        }
        return result;
    }
}
