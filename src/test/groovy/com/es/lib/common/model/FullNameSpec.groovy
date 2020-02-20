package com.es.lib.common.model

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 23.09.16
 */
class FullNameSpec extends Specification {

    def "Если surname == null то в начале не должно быть пробела"() {
        when:
        def entity = new FullName(null, 'name', null)
        then:
        entity.full == 'name'
    }

    def "Если surname != null то в начале должен быть surname"() {
        when:
        def entity = new FullName('surname', 'name', null)
        then:
        entity.full == "surname name"
    }

    def "Если все поля заполнены то полное имя 'surname name patronymic'"() {
        when:
        def entity = new FullName('surname', 'name', 'patronymic')
        then:
        entity.full == "surname name patronymic"
    }

    def "Если surname == null, но patronymic != null то полное имя 'name patronymic'"() {
        when:
        def entity = new FullName(null, 'name', 'patronymic')
        then:
        entity.full == "name patronymic"
    }

    def "toList"() {
        when:
        def entity = new FullName('1', '2', '3')
        then:
        entity.toList() == ['1', '2', '3']
    }

    def "toArray"() {
        when:
        def entity = new FullName('1', '2', '3')
        def result = entity.toArray()
        then:
        result.size() == 3
        result[0] == '1'
        result[1] == '2'
        result[2] == '3'
    }

    def "isAllEmpty"() {
        expect:
        new FullName(surname, name, patronymic).allEmpty == result
        where:
        surname | name | patronymic || result
        null    | null | null        | true
        ''      | null | null        | true
        null    | ''   | null        | true
        null    | null | ''          | true
        ''      | ''   | null        | true
        ''      | ''   | ''          | true
        'a'     | ''   | ''          | false
        'a'     | null | null        | false
    }

    def "isAllBlank"() {
        expect:
        new FullName(surname, name, patronymic).allBlank == result
        where:
        surname | name | patronymic || result
        null    | null | null        | true
        ' '     | null | null        | true
        null    | ' '  | null        | true
        null    | null | '  '        | true
        '   '   | '  ' | null        | true
        '    '  | ''   | '   '       | true
        'a'     | '  ' | '  '        | false
        'a'     | null | null        | false
    }

    def "Сокращения с правой стороны"() {
        expect:
        new FullName(value).initialsRight == result
        where:
        value                        | result
        null                         | ""
        ""                           | ""
        "aaa"                        | "aaa"
        "aaa bbb"                    | "aaa b."
        "aaa \t bbb"                 | "aaa b."
        "aaa bbb ccc"                | "aaa b. c."
        "aaa   bbb   ccc"            | "aaa b. c."
        "aaa  \n bbb   ccc"          | "aaa b. c."
        "aaa   bbb   ccc "           | "aaa b. c."
        "aaa   bbb   ccc           " | "aaa b. c."
        "Рахметов Иприс Алюб Углы"   | "Рахметов И. А."
    }

    def "Сокращения с левой стороны"() {
        expect:
        new FullName(value).initialsLeft == result
        where:
        value                        | result
        null                         | ""
        ""                           | ""
        "aaa"                        | "aaa"
        "aaa bbb"                    | "b. aaa"
        "aaa \t bbb"                 | "b. aaa"
        "aaa bbb ccc"                | "b. c. aaa"
        "aaa   bbb   ccc"            | "b. c. aaa"
        "aaa  \n bbb   ccc"          | "b. c. aaa"
        "aaa   bbb   ccc "           | "b. c. aaa"
        "aaa   bbb   ccc           " | "b. c. aaa"
    }

    def "Рахметов Иприс Алюб Углы"() {
        expect:
        new FullName(value).full == result
        where:
        value                      | result
        null                       | ""
        "Рахметов Иприс Алюб Углы" | "Рахметов Иприс Алюб Углы"
    }

    def "Рахметов Иприс Алюб Углы getNotSurname"() {
        expect:
        new FullName(value).notSurname == result
        where:
        value                      | result
        null                       | ""
        "Рахметов Иприс Алюб Углы" | "Иприс Алюб Углы"
    }

    def "Right chop (статика)"() {
        expect:
        FullName.initiator().get(value) == result
        where:
        value                          | result
        ""                             | ""
        "aaa"                          | "aaa"
        "aaa bbb"                      | "aaa b."
        "aaa \t bbb"                   | "aaa b."
        "aaa bbb ccc"                  | "aaa b. c."
        "aaa   bbb   ccc"              | "aaa b. c."
        "aaa  \n bbb   ccc"            | "aaa b. c."
        "aaa   bbb   ccc d."           | "aaa b. c. d."
        "aaa   bbb   ccc d.          " | "aaa b. c. d."
    }

    def "Right chop2 (статика)"() {
        expect:
        FullName.initiator().get(new FullName(s, n, p)) == result
        where:
        s      | n      | p      | result
        null   | null   | null   | ""
        ""     | ""     | ""     | ""
        " "    | " "    | " "    | ""
        "aaa"  | "aaa"  | "aaa"  | "aaa a. a."
        "aaa " | "aaa " | "aaa " | "aaa a. a."
    }

    def "Left chop (static)"() {
        expect:
        FullName.initiator(true).get(value as String) == result
        where:
        value                          | result
        ""                             | ""
        "aaa"                          | "aaa"
        "aaa bbb"                      | "b. aaa"
        "aaa \t bbb"                   | "b. aaa"
        "aaa bbb ccc"                  | "b. c. aaa"
        "aaa   bbb   ccc"              | "b. c. aaa"
        "aaa  \n bbb   ccc"            | "b. c. aaa"
        "aaa   bbb   ccc d."           | "b. c. d. aaa"
        "aaa   bbb   ccc d.          " | "b. c. d. aaa"
    }
}
