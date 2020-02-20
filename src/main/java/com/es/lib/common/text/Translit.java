package com.es.lib.common.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Translit {

    private static final Map<Character, String> CHAR_MAP = new HashMap<>();

    static {
        CHAR_MAP.put("А".charAt(0), "A");
        CHAR_MAP.put("Б".charAt(0), "B");
        CHAR_MAP.put("В".charAt(0), "V");
        CHAR_MAP.put("Г".charAt(0), "G");
        CHAR_MAP.put("Д".charAt(0), "D");
        CHAR_MAP.put("Е".charAt(0), "E");
        CHAR_MAP.put("Ё".charAt(0), "YO");
        CHAR_MAP.put("Ж".charAt(0), "ZH");
        CHAR_MAP.put("З".charAt(0), "Z");
        CHAR_MAP.put("И".charAt(0), "I");
        CHAR_MAP.put("Й".charAt(0), "Y");
        CHAR_MAP.put("К".charAt(0), "K");
        CHAR_MAP.put("Л".charAt(0), "L");
        CHAR_MAP.put("М".charAt(0), "M");
        CHAR_MAP.put("Н".charAt(0), "N");
        CHAR_MAP.put("О".charAt(0), "O");
        CHAR_MAP.put("П".charAt(0), "P");
        CHAR_MAP.put("Р".charAt(0), "R");
        CHAR_MAP.put("С".charAt(0), "S");
        CHAR_MAP.put("Т".charAt(0), "T");
        CHAR_MAP.put("У".charAt(0), "U");
        CHAR_MAP.put("Ф".charAt(0), "F");
        CHAR_MAP.put("Х".charAt(0), "H");
        CHAR_MAP.put("Ц".charAt(0), "C");
        CHAR_MAP.put("Ч".charAt(0), "CH");
        CHAR_MAP.put("Ш".charAt(0), "SH");
        CHAR_MAP.put("Щ".charAt(0), "SCH");
        CHAR_MAP.put("Ъ".charAt(0), "");
        CHAR_MAP.put("Ы".charAt(0), "Y");
        CHAR_MAP.put("Ь".charAt(0), "");
        CHAR_MAP.put("Э".charAt(0), "E");
        CHAR_MAP.put("Ю".charAt(0), "YU");
        CHAR_MAP.put("Я".charAt(0), "YA");
        Map<Character, String> lower = CHAR_MAP.entrySet().stream().collect(
            Collectors.toMap(
                k -> k.getKey().toString().toLowerCase().toCharArray()[0],
                v -> v.getValue().toLowerCase()
            )
        );
        CHAR_MAP.putAll(lower);
    }

    static String convert(String value, Function<String, String> caseConverter, Function<String, String> whitespaceConverter) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return applyConverter(
            applyConverter(
                transform(value),
                caseConverter
            ),
            whitespaceConverter
        );
    }

    private static String transform(String value) {
        return value.chars().mapToObj(Translit::map).collect(Collectors.joining());
    }

    private static String applyConverter(String value, Function<String, String> converter) {
        if (converter == null) {
            return value;
        }
        return converter.apply(value);
    }

    private static String map(Integer v) {
        final String result = CHAR_MAP.get(Character.toChars(v)[0]);
        if (result == null) {
            return String.valueOf(Character.toChars(v));
        }
        return result;
    }
}
