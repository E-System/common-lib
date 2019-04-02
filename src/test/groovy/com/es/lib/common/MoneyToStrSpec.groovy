/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2018
 */

package com.es.lib.common

import spock.lang.Specification

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 22.11.18
 */
class MoneyToStrSpec extends Specification {

    def "Format"() {
        expect:
        (new MoneyToStr(MoneyToStr.Currency.RUR, MoneyToStr.Language.RUS, MoneyToStr.Pennies.NUMBER)).convert(a) == b
        where:
        a              | b
        0.01d          | "ноль рублей 01 копейка"
        0.02d          | "ноль рублей 02 копейки"
        0.03d          | "ноль рублей 03 копейки"
        0.05d          | "ноль рублей 05 копеек"
        0.07d          | "ноль рублей 07 копеек"
        0.1d           | "ноль рублей 10 копеек"
        0.2d           | "ноль рублей 20 копеек"
        0.3d           | "ноль рублей 30 копеек"
        0.5d           | "ноль рублей 50 копеек"
        0.7d           | "ноль рублей 70 копеек"
        1.0d           | "один рубль 00 копеек"
        1.11d          | "один рубль 11 копеек"
        2.0d           | "два рубля 00 копеек"
        2.11d          | "два рубля 11 копеек"
        20.11d         | "двадцать рублей 11 копеек"
        200.11d        | "двести рублей 11 копеек"
        2000.11d       | "две тысячи рублей 11 копеек"
        20000.11d      | "двадцать тысяч рублей 11 копеек"
        200000.11d     | "двести тысяч рублей 11 копеек"
        2000000.11d    | "два миллиона рублей 11 копеек"
        -949.85d       | "минус девятьсот сорок девять рублей 85 копеек"

    }

    def "Format BYN RUS"() {
        expect:
        (new MoneyToStr(MoneyToStr.Currency.BYN, MoneyToStr.Language.RUS, MoneyToStr.Pennies.NUMBER)).convert(a) == b
        where:
        a              | b
        0.01d          | "ноль белорусских рублей 01 копейка"
        0.02d          | "ноль белорусских рублей 02 копейки"
        0.03d          | "ноль белорусских рублей 03 копейки"
        0.05d          | "ноль белорусских рублей 05 копеек"
        0.07d          | "ноль белорусских рублей 07 копеек"
        0.1d           | "ноль белорусских рублей 10 копеек"
        0.2d           | "ноль белорусских рублей 20 копеек"
        0.3d           | "ноль белорусских рублей 30 копеек"
        0.5d           | "ноль белорусских рублей 50 копеек"
        0.7d           | "ноль белорусских рублей 70 копеек"
        1.0d           | "один белорусский рубль 00 копеек"
        1.11d          | "один белорусский рубль 11 копеек"
        2.0d           | "два белорусских рубля 00 копеек"
        2.11d          | "два белорусских рубля 11 копеек"
        20.11d         | "двадцать белорусских рублей 11 копеек"
        200.11d        | "двести белорусских рублей 11 копеек"
        2000.11d       | "две тысячи белорусских рублей 11 копеек"
        20000.11d      | "двадцать тысяч белорусских рублей 11 копеек"
        200000.11d     | "двести тысяч белорусских рублей 11 копеек"
        2000000.11d    | "два миллиона белорусских рублей 11 копеек"
        -949.85d       | "минус девятьсот сорок девять белорусских рублей 85 копеек"
    }

    def "Format BYN RUS names"() {
        expect:
        (new MoneyToStr(MoneyToStr.Currency.BYN, MoneyToStr.Language.RUS, MoneyToStr.Pennies.NUMBER)).convertName(a) == b
        where:
        a              | b
        0.01d          | "белорусских рублей"
        0.02d          | "белорусских рублей"
        0.03d          | "белорусских рублей"
        0.05d          | "белорусских рублей"
        0.07d          | "белорусских рублей"
        0.1d           | "белорусских рублей"
        0.2d           | "белорусских рублей"
        0.3d           | "белорусских рублей"
        0.5d           | "белорусских рублей"
        0.7d           | "белорусских рублей"
        1.0d           | "белорусский рубль"
        1.11d          | "белорусский рубль"
        2.0d           | "белорусских рубля"
        2.11d          | "белорусских рубля"
        20.11d         | "белорусских рублей"
        200.11d        | "белорусских рублей"
        2000.11d       | "белорусских рублей"
        20000.11d      | "белорусских рублей"
        200000.11d     | "белорусских рублей"
        2000000.11d    | "белорусских рублей"
        -949.85d       | "минус белорусских рублей"
    }


}
