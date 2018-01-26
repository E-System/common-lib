/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.text;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class Transliterator {

    public static final String ENGLISH = "`qwertyuiop[]asdfghjkl;'zxcvbnm,.~QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>";
    public static final String RUSSIAN = "ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";

    private Transliterator() { }

    /**
     * Транслитерация по символам на клавиатуре(переводит и символа в ENGLISH в символ RUSSIAN в той же позиции)
     *
     * @param term             исходный текст
     * @param englishToRussian признак перевода английских букв в русские
     * @return результат транслитерации
     */
    public static CharSequence get(CharSequence term, boolean englishToRussian) {
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

    public static Collection<? extends CharSequence> get(String term) {
        return Arrays.asList(
            term,
            get(term, true),
            get(term, false)
        );
    }
}
