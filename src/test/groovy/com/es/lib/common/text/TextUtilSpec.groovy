package com.es.lib.common.text

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

class TextUtilSpec extends Specification {

    def "Evaluate int"() {
        expect:
        TextUtil.pluralize(value, str1, str2, str3) == result
        where:
        value | str1   | str2  | str3   | result
        0     | 'день' | 'дня' | 'дней' | 'дней'
        1     | 'день' | 'дня' | 'дней' | 'день'
        2     | 'день' | 'дня' | 'дней' | 'дня'
        3     | 'день' | 'дня' | 'дней' | 'дня'
        4     | 'день' | 'дня' | 'дней' | 'дня'
        5     | 'день' | 'дня' | 'дней' | 'дней'
        6     | 'день' | 'дня' | 'дней' | 'дней'
        7     | 'день' | 'дня' | 'дней' | 'дней'
        8     | 'день' | 'дня' | 'дней' | 'дней'
        9     | 'день' | 'дня' | 'дней' | 'дней'
        10    | 'день' | 'дня' | 'дней' | 'дней'
        101   | 'день' | 'дня' | 'дней' | 'день'
        102   | 'день' | 'дня' | 'дней' | 'дня'
    }

    def "Evaluate long"() {
        expect:
        TextUtil.pluralize(value, str1, str2, str3) == result
        where:
        value | str1   | str2  | str3   | result
        0L    | 'день' | 'дня' | 'дней' | 'дней'
        1L    | 'день' | 'дня' | 'дней' | 'день'
        2L    | 'день' | 'дня' | 'дней' | 'дня'
        3L    | 'день' | 'дня' | 'дней' | 'дня'
        4L    | 'день' | 'дня' | 'дней' | 'дня'
        5L    | 'день' | 'дня' | 'дней' | 'дней'
        6L    | 'день' | 'дня' | 'дней' | 'дней'
        7L    | 'день' | 'дня' | 'дней' | 'дней'
        8L    | 'день' | 'дня' | 'дней' | 'дней'
        9L    | 'день' | 'дня' | 'дней' | 'дней'
        10L   | 'день' | 'дня' | 'дней' | 'дней'
        101L  | 'день' | 'дня' | 'дней' | 'день'
        102L  | 'день' | 'дня' | 'дней' | 'дня'
    }

    def "RemoveDelimiters"() {
        expect:
        TextUtil.removeDelimiters(value) == result
        where:
        value          | result
        'г.,'          | 'г'
        'г,'           | 'г'
        'г'            | 'г'
        ''             | ''
        'г. Барнаул'   | 'г Барнаул'
        ',г. Барнаул.' | 'г Барнаул'
    }

    def "ReplaceDelimiters"() {
        expect:
        TextUtil.replaceDelimiters(value, target) == result
        where:
        value         | target | result
        'г.,'         | '|'    | 'г||'
        'г,'          | '|'    | 'г|'
        'г'           | '|'    | 'г'
        ''            | '|'    | ''
        'г. Барнаул'  | '|'    | 'г| Барнаул'
        ',г.Барнаул.' | '|'    | '|г|Барнаул|'
    }

    def "Simple keyboard symbol convert"() {
        expect:
        TextUtil.keyboard(value, englishToRussian) == result
        where:
        value                  | englishToRussian || result
        ""                     | true             || ""
        "asdf"                 | true             || "фыва"
        "фыва"                 | true             || "фыва"
        KeyboardLayout.ENGLISH | true             || KeyboardLayout.RUSSIAN
        KeyboardLayout.RUSSIAN | true             || KeyboardLayout.RUSSIAN
        ""                     | false            || ""
        "asdf"                 | false            || "asdf"
        "фыва"                 | false            || "asdf"
        KeyboardLayout.RUSSIAN | false            || KeyboardLayout.ENGLISH
        KeyboardLayout.ENGLISH | false            || KeyboardLayout.ENGLISH
    }

    def "Simple keyboard symbol convert (upper case with Shift)"() {
        expect:
        TextUtil.keyboard(value, englishToRussian) == result
        where:
        value    | englishToRussian || result
        ""       | true             || ""
        "ASDF"   | true             || "ФЫВА"
        "ФЫВА"   | true             || "ФЫВА"
        "ФывА"   | true             || "ФывА"
        "~:\"<>" | true             || "ЁЖЭБЮ"
        ""       | false            || ""
        "ASDF"   | false            || "ASDF"
        "ФывА"   | false            || "AsdF"
        "ЁЖЭБЮ"  | false            || "~:\"<>"
    }

    def "Split"() {
        expect:
        splitter.toList(value) == result
        where:
        value   | splitter                     || result
        ""      | TextUtil.splitBy(";")        || []
        "1;2"   | TextUtil.splitBy(";")        || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";")        || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";", true)  || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";", false) || ["1", " 2"]
        "1;; 2" | TextUtil.splitBy(";", 3)     || ["1", "", "2"]

    }

    def "Split1"() {
        expect:
        splitter.toPairs(value) == result
        where:
        value        | splitter                           || result
        ""           | TextUtil.splitBy(",").splitBy(":") || []
        "1:2,2:3"    | TextUtil.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
        "1:2, 2 : 3" | TextUtil.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
    }
}
