package com.es.lib.common.text

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import java.util.function.Function

class TextsSpec extends Specification {

    static class SplitItem {
        int v1
        int v2

        SplitItem(int v1, int v2) {
            this.v1 = v1
            this.v2 = v2
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            SplitItem splitItem = (SplitItem) o

            if (v1 != splitItem.v1) return false
            if (v2 != splitItem.v2) return false

            return true
        }

        int hashCode() {
            int result
            result = v1
            result = 31 * result + v2
            return result
        }
    }

    def "Evaluate int"() {
        expect:
        Texts.pluralize(value, str1, str2, str3) == result
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
        Texts.pluralize(value, str1, str2, str3) == result
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
        Texts.removeDelimiters(value) == result
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
        Texts.replaceDelimiters(value, target) == result
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
        Texts.keyboard(value, englishToRussian) == result
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
        Texts.keyboard(value, englishToRussian) == result
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
        value   | splitter                  || result
        ""      | Texts.splitBy(";")        || []
        "1;2"   | Texts.splitBy(";")        || ["1", "2"]
        "1; 2"  | Texts.splitBy(";")        || ["1", "2"]
        "1; 2"  | Texts.splitBy(";", true)  || ["1", "2"]
        "1; 2"  | Texts.splitBy(";", false) || ["1", " 2"]
        "1;; 2" | Texts.splitBy(";", 3)     || ["1", "", "2"]

    }

    def "Split to object"() {
        expect:
        splitter.toObject(value, new Function<String[], SplitItem>() {
            @Override
            SplitItem apply(String[] v) {
                return new SplitItem(Integer.parseInt(v[0]), Integer.parseInt(v[1]))
            }
        }) == result
        where:
        value   | splitter                  || result
        ""      | Texts.splitBy(";")        || null
        "1;2"   | Texts.splitBy(";")        || new SplitItem(1, 2)
        "1; 2"  | Texts.splitBy(";")        || new SplitItem(1, 2)
        "1; 2"  | Texts.splitBy(";", true)  || new SplitItem(1, 2)
        "1; 2"  | Texts.splitBy(";", false) || null
        "1;; 2" | Texts.splitBy(";", 3)     || null
    }

    def "Split1"() {
        expect:
        splitter.toPairs(value) == result
        where:
        value        | splitter                        || result
        ""           | Texts.splitBy(",").splitBy(":") || []
        "1:2,2:3"    | Texts.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
        "1:2, 2 : 3" | Texts.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
    }

    def "Split on 2 rows"() {
        expect:
        Texts.rows(str, space, len) == result
        where:
        str            | space | len | result
        ""             | ""    | 0   | ["", ""].toArray()
        null           | ""    | 0   | ["", ""].toArray()

        "a"            | ""    | 0   | ["", "a"].toArray()
        "a"            | ""    | 1   | ["a", ""].toArray()

        "asd asd asd"  | ""    | 0   | ["", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 1   | ["", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 2   | ["", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 3   | ["asd", "asd asd"].toArray()
        "asd asd asd"  | ""    | 4   | ["asd", "asd asd"].toArray()
        "asd asd asd"  | ""    | 5   | ["asd", "asd asd"].toArray()
        "asd asd asd"  | ""    | 6   | ["asd", "asd asd"].toArray()
        "asd asd asd"  | ""    | 7   | ["asd asd", "asd"].toArray()
        "asd asd asd"  | ""    | 8   | ["asd asd", "asd"].toArray()
        "asd asd asd"  | ""    | 9   | ["asd asd", "asd"].toArray()
        "asd asd asd"  | ""    | 10  | ["asd asd", "asd"].toArray()
        "asd asd asd"  | ""    | 11  | ["asd asd asd", ""].toArray()
        "asd asd asd"  | ""    | 12  | ["asd asd asd", ""].toArray()

        "asd  asd asd" | ""    | 0   | ["", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 1   | ["", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 2   | ["", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 3   | ["asd", " asd asd"].toArray()
        "asd  asd asd" | ""    | 4   | ["asd", " asd asd"].toArray()
        "asd  asd asd" | ""    | 5   | ["asd ", "asd asd"].toArray()
        "asd  asd asd" | ""    | 6   | ["asd ", "asd asd"].toArray()
        "asd  asd asd" | ""    | 7   | ["asd ", "asd asd"].toArray()
        "asd  asd asd" | ""    | 8   | ["asd  asd", "asd"].toArray()
        "asd  asd asd" | ""    | 9   | ["asd  asd", "asd"].toArray()
        "asd  asd asd" | ""    | 10  | ["asd  asd", "asd"].toArray()
        "asd  asd asd" | ""    | 11  | ["asd  asd", "asd"].toArray()
        "asd  asd asd" | ""    | 12  | ["asd  asd asd", ""].toArray()
        "asd  asd asd" | ""    | 13  | ["asd  asd asd", ""].toArray()
        "asd  asd asd" | ""    | 14  | ["asd  asd asd", ""].toArray()
    }

    def "Split on 3 rows"() {
        expect:
        Texts.rows(str, space, len, len2) == result
        where:
        str            | space | len | len2 | result
        ""             | ""    | 0   | 0    | ["", "", ""].toArray()
        null           | ""    | 0   | 0    | ["", "", ""].toArray()

        "a"            | ""    | 0   | 0    | ["", "", "a"].toArray()
        "a"            | ""    | 1   | 0    | ["a", "", ""].toArray()

        "asd asd asd"  | ""    | 0   | 0    | ["", "", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 1   | 0    | ["", "", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 2   | 0    | ["", "", "asd asd asd"].toArray()
        "asd asd asd"  | ""    | 3   | 0    | ["asd", "", "asd asd"].toArray()
        "asd asd asd"  | ""    | 3   | 3    | ["asd", "asd", "asd"].toArray()
        "asd asd asd"  | ""    | 4   | 0    | ["asd", "", "asd asd"].toArray()
        "asd asd asd"  | ""    | 4   | 3    | ["asd", "asd", "asd"].toArray()
        "asd asd asd"  | ""    | 5   | 0    | ["asd", "", "asd asd"].toArray()
        "asd asd asd"  | ""    | 6   | 0    | ["asd", "", "asd asd"].toArray()
        "asd asd asd"  | ""    | 7   | 0    | ["asd asd", "", "asd"].toArray()
        "asd asd asd"  | ""    | 8   | 0    | ["asd asd", "", "asd"].toArray()
        "asd asd asd"  | ""    | 9   | 0    | ["asd asd", "", "asd"].toArray()
        "asd asd asd"  | ""    | 10  | 0    | ["asd asd", "", "asd"].toArray()
        "asd asd asd"  | ""    | 11  | 0    | ["asd asd asd", "", ""].toArray()
        "asd asd asd"  | ""    | 12  | 0    | ["asd asd asd", "", ""].toArray()

        "asd  asd asd" | ""    | 0   | 0    | ["", "", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 1   | 0    | ["", "", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 2   | 0    | ["", "", "asd  asd asd"].toArray()
        "asd  asd asd" | ""    | 3   | 0    | ["asd", "", "asd asd"].toArray()
        "asd  asd asd" | ""    | 3   | 3    | ["asd", "asd", "asd"].toArray()
        "asd  asd asd" | ""    | 4   | 0    | ["asd", "", "asd asd"].toArray()
        "asd  asd asd" | ""    | 5   | 0    | ["asd ", "", "asd asd"].toArray()
        "asd  asd asd" | ""    | 6   | 0    | ["asd ", "", "asd asd"].toArray()
        "asd  asd asd" | ""    | 7   | 0    | ["asd ", "", "asd asd"].toArray()
        "asd  asd asd" | ""    | 8   | 0    | ["asd  asd", "", "asd"].toArray()
        "asd  asd asd" | ""    | 9   | 0    | ["asd  asd", "", "asd"].toArray()
        "asd  asd asd" | ""    | 10  | 0    | ["asd  asd", "", "asd"].toArray()
        "asd  asd asd" | ""    | 11  | 0    | ["asd  asd", "", "asd"].toArray()
        "asd  asd asd" | ""    | 12  | 0    | ["asd  asd asd", "", ""].toArray()
        "asd  asd asd" | ""    | 13  | 0    | ["asd  asd asd", "", ""].toArray()
        "asd  asd asd" | ""    | 14  | 0    | ["asd  asd asd", "", ""].toArray()

        "asd  asd asd" | " "   | 0   | 0    | [" ", " ", "asd  asd asd"].toArray()
        "asd  asd asd" | " "   | 1   | 0    | [" ", " ", "asd  asd asd"].toArray()
        "asd  asd asd" | " "   | 2   | 0    | [" ", " ", "asd  asd asd"].toArray()
        "asd  asd asd" | " "   | 3   | 0    | ["asd", " ", "asd asd"].toArray()
        "asd  asd asd" | " "   | 3   | 3    | ["asd", "asd", "asd"].toArray()
        "asd  asd asd" | " "   | 4   | 0    | ["asd", " ", "asd asd"].toArray()
        "asd  asd asd" | " "   | 5   | 0    | ["asd ", " ", "asd asd"].toArray()
        "asd  asd asd" | " "   | 6   | 0    | ["asd ", " ", "asd asd"].toArray()
        "asd  asd asd" | " "   | 7   | 0    | ["asd ", " ", "asd asd"].toArray()
        "asd  asd asd" | " "   | 8   | 0    | ["asd  asd", " ", "asd"].toArray()
        "asd  asd asd" | " "   | 9   | 0    | ["asd  asd", " ", "asd"].toArray()
        "asd  asd asd" | " "   | 10  | 0    | ["asd  asd", " ", "asd"].toArray()
        "asd  asd asd" | " "   | 11  | 0    | ["asd  asd", " ", "asd"].toArray()
        "asd  asd asd" | " "   | 12  | 0    | ["asd  asd asd", " ", " "].toArray()
        "asd  asd asd" | " "   | 13  | 0    | ["asd  asd asd", " ", " "].toArray()
        "asd  asd asd" | " "   | 14  | 0    | ["asd  asd asd", " ", " "].toArray()
    }

    def "Transliterate without transform"() {
        expect:
        Texts.transliterate(value) == result
        where:
        value                                                                                  || result
        null                                                                                   || null
        ''                                                                                     || ''
        'Привет'                                                                               || 'Privet'
        'Съешь же ещё этих мягких французских булок да выпей чаю'                              || 'Sesh zhe eschyo etih myagkih francuzskih bulok da vypey chayu'
        'Широкая электрификация южных губерний даст мощный толчок подъёму сельского хозяйства' || 'SHirokaya elektrifikaciya yuzhnyh guberniy dast moschnyy tolchok podyomu selskogo hozyaystva'
        'Аэрофотосъёмка ландшафта уже выявила земли богачей и процветающих крестьян'           || 'Aerofotosyomka landshafta uzhe vyyavila zemli bogachey i procvetayuschih krestyan'
    }

    def "Transliterate to lowercase"() {
        expect:
        Texts.transliterateToLower(value) == result
        where:
        value                                                                                  || result
        null                                                                                   || null
        ''                                                                                     || ''
        'Привет'                                                                               || 'privet'
        'Съешь же ещё этих мягких французских булок да выпей чаю'                              || 'sesh zhe eschyo etih myagkih francuzskih bulok da vypey chayu'
        'Широкая электрификация южных губерний даст мощный толчок подъёму сельского хозяйства' || 'shirokaya elektrifikaciya yuzhnyh guberniy dast moschnyy tolchok podyomu selskogo hozyaystva'
        'Аэрофотосъёмка ландшафта уже выявила земли богачей и процветающих крестьян'           || 'aerofotosyomka landshafta uzhe vyyavila zemli bogachey i procvetayuschih krestyan'
    }

    def "Transliterate to uppercase"() {
        expect:
        Texts.transliterateToUpper(value) == result
        where:
        value                                                                                  || result
        null                                                                                   || null
        ''                                                                                     || ''
        'Привет'                                                                               || 'PRIVET'
        'Съешь же ещё этих мягких французских булок да выпей чаю'                              || 'SESH ZHE ESCHYO ETIH MYAGKIH FRANCUZSKIH BULOK DA VYPEY CHAYU'
        'Широкая электрификация южных губерний даст мощный толчок подъёму сельского хозяйства' || 'SHIROKAYA ELEKTRIFIKACIYA YUZHNYH GUBERNIY DAST MOSCHNYY TOLCHOK PODYOMU SELSKOGO HOZYAYSTVA'
        'Аэрофотосъёмка ландшафта уже выявила земли богачей и процветающих крестьян'           || 'AEROFOTOSYOMKA LANDSHAFTA UZHE VYYAVILA ZEMLI BOGACHEY I PROCVETAYUSCHIH KRESTYAN'
    }

    def "Transliterate to uppercase with underscore"() {
        expect:
        Texts.transliterateToUpper(value, "_") == result
        where:
        value                                                                                  || result
        null                                                                                   || null
        ''                                                                                     || ''
        'Привет'                                                                               || 'PRIVET'
        'Привет  привет'                                                                       || 'PRIVET__PRIVET'
        'Съешь же ещё этих мягких французских булок да выпей чаю'                              || 'SESH_ZHE_ESCHYO_ETIH_MYAGKIH_FRANCUZSKIH_BULOK_DA_VYPEY_CHAYU'
        'Широкая электрификация южных губерний даст мощный толчок подъёму сельского хозяйства' || 'SHIROKAYA_ELEKTRIFIKACIYA_YUZHNYH_GUBERNIY_DAST_MOSCHNYY_TOLCHOK_PODYOMU_SELSKOGO_HOZYAYSTVA'
        'Аэрофотосъёмка ландшафта уже выявила земли богачей и процветающих крестьян'           || 'AEROFOTOSYOMKA_LANDSHAFTA_UZHE_VYYAVILA_ZEMLI_BOGACHEY_I_PROCVETAYUSCHIH_KRESTYAN'
    }

    Function<String, Object> RESOLVER = new Function<String, Object>() {

        @Override
        Object apply(String s) {
            if (s == "com.es.profiles.active") {
                return "develop"
            }
            return "NOT_FOUND"
        }
    }

    def "Variable resolver extract"() {
        expect:
        VariableResolver.extract(seq) == value
        where:
        seq                                 | value
        'jndi/db-${com.es.profiles.active}' | ['${com.es.profiles.active}': 'com.es.profiles.active']
        'jndi/db-${asd}'                    | ['${asd}': 'asd']
    }

    def "Resolve variables"() {
        expect:
        Texts.resolveVariables(seq, RESOLVER) == value
        where:
        seq                                  | value
        null                                 | null
        ""                                   | ""
        ['hello': 'hello']                   | ['hello': 'hello']
        'jndi/db-${com.es.profiles.active}'  | 'jndi/db-develop'
        'jndi/db-${com.es.profiles.active1}' | 'jndi/db-NOT_FOUND'
    }

    def "Nbsp"() {
        when:
        def text = "Hello" + Texts.nbsp() + "2"
        def text2 = "Hello" + " " + "2"
        then:
        text.split(" ").size() == 1
        text2.split(" ").size() == 2
        println(text)
    }

    def "Contains"() {
        expect:
        !Texts.contains(null, null)
        !Texts.contains("", null)
        !Texts.contains(null, "")
        Texts.contains("", "")
        Texts.contains("asd", "as")
        Texts.contains("Asd", "as")
        !Texts.contains("Asd", "as", false)
        Texts.contains("Asd", "aS")
        !Texts.contains("Asd", "aS", false)
        !Texts.contains("asd", "asb")
    }
}
