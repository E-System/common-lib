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


}
