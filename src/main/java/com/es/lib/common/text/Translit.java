package com.es.lib.common.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Translit {

    private static final Map<Character, String> CHAR_MAP = new HashMap<>();

    static {
        CHAR_MAP.put('А', "A");
        CHAR_MAP.put('Б', "B");
        CHAR_MAP.put('В', "V");
        CHAR_MAP.put('Г', "G");
        CHAR_MAP.put('Д', "D");
        CHAR_MAP.put('Е', "E");
        CHAR_MAP.put('Ё', "YO");
        CHAR_MAP.put('Ж', "ZH");
        CHAR_MAP.put('З', "Z");
        CHAR_MAP.put('И', "I");
        CHAR_MAP.put('Й', "Y");
        CHAR_MAP.put('К', "K");
        CHAR_MAP.put('Л', "L");
        CHAR_MAP.put('М', "M");
        CHAR_MAP.put('Н', "N");
        CHAR_MAP.put('О', "O");
        CHAR_MAP.put('П', "P");
        CHAR_MAP.put('Р', "R");
        CHAR_MAP.put('С', "S");
        CHAR_MAP.put('Т', "T");
        CHAR_MAP.put('У', "U");
        CHAR_MAP.put('Ф', "F");
        CHAR_MAP.put('Х', "H");
        CHAR_MAP.put('Ц', "C");
        CHAR_MAP.put('Ч', "CH");
        CHAR_MAP.put('Ш', "SH");
        CHAR_MAP.put('Щ', "SCH");
        CHAR_MAP.put('Ъ', "");
        CHAR_MAP.put('Ы', "Y");
        CHAR_MAP.put('Ь', "");
        CHAR_MAP.put('Э', "E");
        CHAR_MAP.put('Ю', "YU");
        CHAR_MAP.put('Я', "YA");
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