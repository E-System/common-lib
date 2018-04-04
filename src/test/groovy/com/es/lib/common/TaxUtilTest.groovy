package com.es.lib.common

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 04.04.2018
 */
class TaxUtilTest extends Specification {

    def "Exclude double"() {
        expect:
        TaxUtil.exclude(value as double, percent as double, onlyPercent) == result as double
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

    def "Exclude long"() {
        expect:
        TaxUtil.exclude(value as long, percent as double, onlyPercent) == result as long
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

    def "Exclude int"() {
        expect:
        TaxUtil.exclude(value as int, percent as double, onlyPercent) == result as int
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

    def "Include double"() {
        expect:
        TaxUtil.include(value as double, percent as double, onlyPercent) == result as double
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

    def "Include long"() {
        expect:
        TaxUtil.include(value as long, percent as double, onlyPercent) == result as long
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

    def "Include int"() {
        expect:
        TaxUtil.include(value as int, percent as double, onlyPercent) == result as int
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
}
