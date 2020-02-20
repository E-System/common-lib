package com.es.lib.common.text;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
public final class TextUtil {

    private TextUtil() { }

    public static String removeDelimiters(String value) {
        return replaceDelimiters(value, "");
    }

    public static String removeDelimiters(String value, String pattern) {
        return replaceDelimiters(value, "", pattern);
    }

    public static String replaceDelimiters(String value, String target) {
        return replaceDelimiters(value, target, "[,.]");
    }

    public static String replaceDelimiters(String value, String target, String pattern) {
        if (value != null) {
            value = value.replaceAll(pattern, target);
        }
        return value;
    }

    public static Splitter splitBy(String regexp) {
        return new Splitter(regexp, null, true);
    }

    public static Splitter splitBy(String regexp, boolean trim) {
        return new Splitter(regexp, null, trim);
    }

    public static Splitter splitBy(String regexp, Integer limit) {
        return new Splitter(regexp, limit, true);
    }

    public static Splitter splitBy(String regexp, Integer limit, boolean trim) {
        return new Splitter(regexp, limit, trim);
    }

    public static String pluralize(int value, String str1, String str2, String str3) {
        return pluralize((long) value, str1, str2, str3);
    }

    public static String pluralize(long value, String str1, String str2, String str3) {
        return Plural.convert(value, str1, str2, str3);
    }

    /**
     * Convert keyboard input from one layout to another по символам на клавиатуре(переводит и символа в ENGLISH в символ RUSSIAN в той же позиции)
     *
     * @param term             Input text
     * @param englishToRussian Flag english to russian
     * @return Converted text
     */
    public static CharSequence keyboard(CharSequence term, boolean englishToRussian) {
        return KeyboardLayout.convert(term, englishToRussian);
    }

    public static Collection<? extends CharSequence> keyboard(String term) {
        return KeyboardLayout.convert(term);
    }

    public static String transliterate(String value) {
        return transliterate(value, null, null);
    }

    public static String transliterate(String value, Function<String, String> caseConverter, Function<String, String> whitespaceConverter) {
        return Translit.convert(value, caseConverter, whitespaceConverter);
    }
}
