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

package com.es.lib.common;

import com.es.lib.common.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Утилиты для работы с номерами телефонов
 *
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 11.08.15
 */
public final class PhoneUtil {

    private static final Function<String, Map.Entry<String, Boolean>> typeMapper = v -> Pair.of(v, isMobile(v));
    private static final Function<String, Map.Entry<String, Boolean>> cleanTypeMapper = v -> Pair.of(clean(v), isMobile(v));

    private PhoneUtil() {
    }

    /**
     * Очистить номер телефона от дополнительных символов (пробельные символы, скобки, дефисы....)
     *
     * @param value номер телефона
     * @return очищенный номер телефона
     */
    public static String clean(String value) {
        if (value == null) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }

    /**
     * Проверить является ли номер мобильным (в начале 7, 8 или пусто, потом 9 и 7 произвольных цифр)
     *
     * @param value номер телефона
     * @return true - номер мобильный
     */
    public static boolean isMobile(String value) {
        value = clean(value);
        return value != null && value.matches("^(7|8)?9\\d{7,}$");
    }

    /**
     * Разделить входную строку на массив пар (номер, признак мобильного телефона)
     *
     * @param value входная строка
     * @param clean очищать номера в результате
     * @return массив пар (номер, признак мобильного телефона)
     */
    public static Collection<Map.Entry<String, Boolean>> split(String value, boolean clean) {
        final Collection<String> phones = StringSplitter.process(value, new StringSplitter.Splitter("(,|;)"));
        return phones.stream().map(clean ? cleanTypeMapper : typeMapper).collect(Collectors.toList());
    }

    /**
     * Склеить однотипные номера в 2 строки
     *
     * @param values    массив номеров
     * @param delimiter разделитель для номеров в финальных строках
     * @return 1 элемент - обычные номера, 2 элемент - мобильные номера
     */
    public static Map.Entry<String, String> joinByType(Collection<Map.Entry<String, Boolean>> values, String delimiter) {
        if (CollectionUtil.isEmpty(values)) {
            return null;
        }
        return Pair.of(
                values.stream().filter(v -> !v.getValue()).map(Map.Entry::getKey).collect(Collectors.joining(delimiter)),
                values.stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.joining(delimiter))
        );
    }

    /**
     * Преобразовать входную строку номеров телефона в 2 по типам номеров
     *
     * @param value     входная строка
     * @param clean     очищать номера в результате
     * @param delimiter разделитель для номеров в финальных строках
     * @return 1 элемент - обычные номера, 2 элемент - мобильные номера
     */
    public static Map.Entry<String, String> groupByType(String value, boolean clean, String delimiter) {
        return joinByType(
                split(value, clean),
                delimiter
        );
    }
}
