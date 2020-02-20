package com.es.lib.common.text;

import java.util.Arrays;
import java.util.Collection;

final class KeyboardLayout {

    static final String ENGLISH = "`qwertyuiop[]asdfghjkl;'zxcvbnm,.~QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>";
    static final String RUSSIAN = "ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";

    /**
     * Convert keyboard input from one layout to another по символам на клавиатуре(переводит и символа в ENGLISH в символ RUSSIAN в той же позиции)
     *
     * @param term             исходный текст
     * @param englishToRussian признак перевода английских букв в русские
     * @return результат транслитерации
     */
    static CharSequence convert(CharSequence term, boolean englishToRussian) {
        if (term == null || term.length() == 0) {
            return term;
        }
        String search = englishToRussian ? ENGLISH : RUSSIAN;
        String target = englishToRussian ? RUSSIAN : ENGLISH;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < term.length(); ++i) {
            char c = term.charAt(i);
            int index = search.indexOf(c);
            if (index >= 0) {
                sb.append(target.charAt(index));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static Collection<? extends CharSequence> convert(String term) {
        return Arrays.asList(
            term,
            convert(term, true),
            convert(term, false)
        );
    }
}
