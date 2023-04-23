package com.eslibs.common.money

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 04.04.2018
 */
class MoneySpec extends Specification {

    def "Exclude tax double"() {
        expect:
        Money.taxExclude(value as double, percent as double, onlyPercent) == result as double
        where:
        value      | percent | onlyPercent || result
        0.0d       | 0.0d    | true        || 0.0d
        0.0d       | 10.0d   | true        || 0.0d
        0.0d       | 18.0d   | true        || 0.0d
        100.0d     | 0.0d    | true        || 0.0d
        100.0d     | 10.0d   | true        || 10.0d
        100.0d     | 18.0d   | true        || 18.0d
        250.0d     | 0.0d    | true        || 0.0d
        250.0d     | 10.0d   | true        || 25.0d
        250.0d     | 18.0d   | true        || 45.0d
        321.0d     | 0.0d    | true        || 0.0d
        321.0d     | 10.0d   | true        || 32.1d
        321.0d     | 18.0d   | true        || 57.78d
        5500000.0d | 18.0d   | true        || 990000.0d
        0.0d       | 0.0d    | false       || 0.0d
        0.0d       | 10.0d   | false       || 0.0d
        0.0d       | 18.0d   | false       || 0.0d
        100.0d     | 0.0d    | false       || 100.0d
        100.0d     | 10.0d   | false       || 110.0d
        100.0d     | 18.0d   | false       || 118.0d
        250.0d     | 0.0d    | false       || 250.0d
        250.0d     | 10.0d   | false       || 275.0d
        250.0d     | 18.0d   | false       || 295.0d
        321.0d     | 0.0d    | false       || 321.0d
        321.0d     | 10.0d   | false       || 353.1d
        321.0d     | 18.0d   | false       || 378.78d
        5500000.0d | 18.0d   | false       || 6490000.0d
    }

    def "Exclude tax long"() {
        expect:
        Money.taxExclude(value as long, percent as double, onlyPercent) == result as long
        where:
        value | percent | onlyPercent || result
        0L    | 0.0d    | true        || 0L
        0L    | 10.0d   | true        || 0L
        0L    | 18.0d   | true        || 0L
        100L  | 0.0d    | true        || 0L
        100L  | 10.0d   | true        || 10L
        100L  | 18.0d   | true        || 18L
        250L  | 0.0d    | true        || 0L
        250L  | 10.0d   | true        || 25L
        250L  | 18.0d   | true        || 45L
        321L  | 0.0d    | true        || 0L
        321L  | 10.0d   | true        || 32L
        321L  | 18.0d   | true        || 58L
        0L    | 0.0d    | false       || 0L
        0L    | 10.0d   | false       || 0L
        0L    | 18.0d   | false       || 0L
        100L  | 0.0d    | false       || 100L
        100L  | 10.0d   | false       || 110L
        100L  | 18.0d   | false       || 118L
        250L  | 0.0d    | false       || 250L
        250L  | 10.0d   | false       || 275L
        250L  | 18.0d   | false       || 295L
        321L  | 0.0d    | false       || 321L
        321L  | 10.0d   | false       || 353L
        321L  | 18.0d   | false       || 379L
    }

    def "Exclude tax int"() {
        expect:
        Money.taxExclude(value as int, percent as double, onlyPercent) == result as int
        where:
        value | percent | onlyPercent || result
        0     | 0.0d    | true        || 0
        0     | 10.0d   | true        || 0
        0     | 18.0d   | true        || 0
        100   | 0.0d    | true        || 0
        100   | 10.0d   | true        || 10
        100   | 18.0d   | true        || 18
        250   | 0.0d    | true        || 0
        250   | 10.0d   | true        || 25
        250   | 18.0d   | true        || 45
        321   | 0.0d    | true        || 0
        321   | 10.0d   | true        || 32
        321   | 18.0d   | true        || 58
        0     | 0.0d    | false       || 0
        0     | 10.0d   | false       || 0
        0     | 18.0d   | false       || 0
        100   | 0.0d    | false       || 100
        100   | 10.0d   | false       || 110
        100   | 18.0d   | false       || 118
        250   | 0.0d    | false       || 250
        250   | 10.0d   | false       || 275
        250   | 18.0d   | false       || 295
        321   | 0.0d    | false       || 321
        321   | 10.0d   | false       || 353
        321   | 18.0d   | false       || 379
    }

    def "Include tax double"() {
        expect:
        Money.taxInclude(value as double, percent as double, onlyPercent) == result as double
        where:
        value      | percent | onlyPercent || result
        0.0d       | 0.0d    | true        || 0.0d
        0.0d       | 10.0d   | true        || 0.0d
        0.0d       | 18.0d   | true        || 0.0d
        100.0d     | 0.0d    | true        || 0.0d
        100.0d     | 10.0d   | true        || 9.090909090909092d
        100.0d     | 18.0d   | true        || 15.254237288135593d
        250.0d     | 0.0d    | true        || 0.0d
        250.0d     | 10.0d   | true        || 22.727272727272727d
        250.0d     | 18.0d   | true        || 38.13559322033898d
        321.0d     | 0.0d    | true        || 0.0d
        321.0d     | 10.0d   | true        || 29.181818181818183d
        321.0d     | 18.0d   | true        || 48.96610169491525d
        2360000.0d | 18.0d   | true        || 360000.0d
        0.0d       | 0.0d    | false       || 0.0d
        0.0d       | 10.0d   | false       || 0.0d
        0.0d       | 18.0d   | false       || 0.0d
        100.0d     | 0.0d    | false       || 100.0d
        100.0d     | 10.0d   | false       || 90.9090909090909d
        100.0d     | 18.0d   | false       || 84.7457627118644d
        250.0d     | 0.0d    | false       || 250.0d
        250.0d     | 10.0d   | false       || 227.27272727272728d
        250.0d     | 18.0d   | false       || 211.864406779661d
        321.0d     | 0.0d    | false       || 321.0d
        321.0d     | 10.0d   | false       || 291.8181818181818d
        321.0d     | 18.0d   | false       || 272.03389830508473d
        2360000.0d | 18.0d   | false       || 2000000.0d
    }

    def "Include tax long"() {
        expect:
        Money.taxInclude(value as long, percent as double, onlyPercent) == result as long
        where:
        value | percent | onlyPercent || result
        0L    | 0.0d    | true        || 0L
        0L    | 10.0d   | true        || 0L
        0L    | 18.0d   | true        || 0L
        100L  | 0.0d    | true        || 0L
        100L  | 10.0d   | true        || 9L
        100L  | 18.0d   | true        || 15L
        250L  | 0.0d    | true        || 0L
        250L  | 10.0d   | true        || 23L
        250L  | 18.0d   | true        || 38L
        321L  | 0.0d    | true        || 0L
        321L  | 10.0d   | true        || 29L
        321L  | 18.0d   | true        || 49L
        0L    | 0.0d    | false       || 0L
        0L    | 10.0d   | false       || 0L
        0L    | 18.0d   | false       || 0L
        100L  | 0.0d    | false       || 100L
        100L  | 10.0d   | false       || 91L
        100L  | 18.0d   | false       || 85L
        250L  | 0.0d    | false       || 250L
        250L  | 10.0d   | false       || 227L
        250L  | 18.0d   | false       || 212L
        321L  | 0.0d    | false       || 321L
        321L  | 10.0d   | false       || 292L
        321L  | 18.0d   | false       || 272L
    }

    def "Include tax int"() {
        expect:
        Money.taxInclude(value as int, percent as double, onlyPercent) == result as int
        where:
        value | percent | onlyPercent || result
        0     | 0.0d    | true        || 0
        0     | 10.0d   | true        || 0
        0     | 18.0d   | true        || 0
        100   | 0.0d    | true        || 0
        100   | 10.0d   | true        || 9
        100   | 18.0d   | true        || 15
        250   | 0.0d    | true        || 0
        250   | 10.0d   | true        || 23
        250   | 18.0d   | true        || 38
        321   | 0.0d    | true        || 0
        321   | 10.0d   | true        || 29
        321   | 18.0d   | true        || 49
        0     | 0.0d    | false       || 0
        0     | 10.0d   | false       || 0
        0     | 18.0d   | false       || 0
        100   | 0.0d    | false       || 100
        100   | 10.0d   | false       || 91
        100   | 18.0d   | false       || 85
        250   | 0.0d    | false       || 250
        250   | 10.0d   | false       || 227
        250   | 18.0d   | false       || 212
        321   | 0.0d    | false       || 321
        321   | 10.0d   | false       || 292
        321   | 18.0d   | false       || 272
    }

    def "Format"() {
        expect:
        Money.format().convert(a) == b
        where:
        a           | b
        0.01d       | "ноль рублей 01 копейка"
        0.02d       | "ноль рублей 02 копейки"
        0.03d       | "ноль рублей 03 копейки"
        0.05d       | "ноль рублей 05 копеек"
        0.07d       | "ноль рублей 07 копеек"
        0.1d        | "ноль рублей 10 копеек"
        0.2d        | "ноль рублей 20 копеек"
        0.3d        | "ноль рублей 30 копеек"
        0.5d        | "ноль рублей 50 копеек"
        0.7d        | "ноль рублей 70 копеек"
        1.0d        | "один рубль 00 копеек"
        1.11d       | "один рубль 11 копеек"
        2.0d        | "два рубля 00 копеек"
        2.11d       | "два рубля 11 копеек"
        20.11d      | "двадцать рублей 11 копеек"
        200.11d     | "двести рублей 11 копеек"
        2000.11d    | "две тысячи рублей 11 копеек"
        20000.11d   | "двадцать тысяч рублей 11 копеек"
        200000.11d  | "двести тысяч рублей 11 копеек"
        2000000.11d | "два миллиона рублей 11 копеек"
        -949.85d    | "минус девятьсот сорок девять рублей 85 копеек"

    }

    def "Format BYN RUS"() {
        expect:
        Money.format("BYN").convert(a) == b
        where:
        a           | b
        0.01d       | "ноль белорусских рублей 01 копейка"
        0.02d       | "ноль белорусских рублей 02 копейки"
        0.03d       | "ноль белорусских рублей 03 копейки"
        0.05d       | "ноль белорусских рублей 05 копеек"
        0.07d       | "ноль белорусских рублей 07 копеек"
        0.1d        | "ноль белорусских рублей 10 копеек"
        0.2d        | "ноль белорусских рублей 20 копеек"
        0.3d        | "ноль белорусских рублей 30 копеек"
        0.5d        | "ноль белорусских рублей 50 копеек"
        0.7d        | "ноль белорусских рублей 70 копеек"
        1.0d        | "один белорусский рубль 00 копеек"
        1.11d       | "один белорусский рубль 11 копеек"
        2.0d        | "два белорусских рубля 00 копеек"
        2.11d       | "два белорусских рубля 11 копеек"
        20.11d      | "двадцать белорусских рублей 11 копеек"
        200.11d     | "двести белорусских рублей 11 копеек"
        2000.11d    | "две тысячи белорусских рублей 11 копеек"
        20000.11d   | "двадцать тысяч белорусских рублей 11 копеек"
        200000.11d  | "двести тысяч белорусских рублей 11 копеек"
        2000000.11d | "два миллиона белорусских рублей 11 копеек"
        -949.85d    | "минус девятьсот сорок девять белорусских рублей 85 копеек"
    }

    def "Format BYN RUS names"() {
        expect:
        Money.format("BYN").convertName(a) == b
        where:
        a           | b
        0.01d       | "белорусских рублей"
        0.02d       | "белорусских рублей"
        0.03d       | "белорусских рублей"
        0.05d       | "белорусских рублей"
        0.07d       | "белорусских рублей"
        0.1d        | "белорусских рублей"
        0.2d        | "белорусских рублей"
        0.3d        | "белорусских рублей"
        0.5d        | "белорусских рублей"
        0.7d        | "белорусских рублей"
        1.0d        | "белорусский рубль"
        1.11d       | "белорусский рубль"
        2.0d        | "белорусских рубля"
        2.11d       | "белорусских рубля"
        20.11d      | "белорусских рублей"
        200.11d     | "белорусских рублей"
        2000.11d    | "белорусских рублей"
        20000.11d   | "белорусских рублей"
        200000.11d  | "белорусских рублей"
        2000000.11d | "белорусских рублей"
        -949.85d    | "белорусских рублей"
    }

    def "String without numbers"() {
        expect:
        Money.formatFullText().convert(1234.10) == 'одна тысяча двести тридцать четыре рубля десять копеек'
    }

    def "In end is pennies"() {
        expect:
        Money.formatFullText().convert(1234.10).endsWith('копеек')
        Money.formatFullText().convert(1234.11).endsWith('копеек')
        Money.formatFullText().convert(1234.12).endsWith('копеек')
        Money.formatFullText().convert(1234.13).endsWith('копеек')
        Money.formatFullText().convert(1234.14).endsWith('копеек')
        Money.formatFullText().convert(1234.15).endsWith('копеек')
        Money.formatFullText().convert(1234.16).endsWith('копеек')
        Money.formatFullText().convert(1234.17).endsWith('копеек')
        Money.formatFullText().convert(1234.18).endsWith('копеек')
        Money.formatFullText().convert(1234.19).endsWith('копеек')
        Money.formatFullText().convert(1234.20).endsWith('копеек')
    }
}
