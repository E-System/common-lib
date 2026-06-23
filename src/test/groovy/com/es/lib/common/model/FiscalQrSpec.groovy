package com.es.lib.common.model

import spock.lang.Specification

class FiscalQrSpec extends Specification {

    static VALUE = 't=20260607T1303&s=1506.00&fn=7384440900865967&i=1323&fp=2179972036&n=1'
    static MULTIPLE_VALUE = 't=20260623T1128&s=239.40&fn=7380440903639523&i=12643&fp=2358095958&n=1t=20260623T1128&s=239.40&fn=7380440903639523&i=12643&fp=2358095958&n=1t=20260623T1128&s=239.40&fn=7380440903639523&i=12643&fp=2358095958&n=1t=20260623T1128&s=239.40&fn=7380440903639523&i=12643&fp=2358095958&n=1'
    static PREFIX_URL = 'https://asdasdasd.ru'

    def "Parse"() {
        expect:
        FiscalQr.of(null) == null
        FiscalQr.of('') == null
        with(FiscalQr.of(VALUE)) {
            println(it)
            it.date != null
            it.fn == '7384440900865967'
            it.fp == '2179972036'
            it.sum == 150600
            it.doc == '1323'
            it.type == FiscalQr.Type.SELL
            it.asString() == VALUE
        }
        with(FiscalQr.of(PREFIX_URL + '?' + VALUE)) {
            println(it)
            it.date != null
            it.fn == '7384440900865967'
            it.fp == '2179972036'
            it.sum == 150600
            it.doc == '1323'
            it.type == FiscalQr.Type.SELL
            it.asString() == VALUE
            it.asString(PREFIX_URL) == PREFIX_URL + '?' + VALUE
        }
    }

    def "Parse multiple"() {
        expect:
        FiscalQr.of(null) == null
        FiscalQr.of('') == null
        with(FiscalQr.of(MULTIPLE_VALUE)) {
            println(it)
            it.date != null
            it.fn == '7380440903639523'
            it.fp == '2358095958'
            it.sum == 23940
            it.doc == '12643'
            it.type == FiscalQr.Type.SELL
        }
        with(FiscalQr.of(PREFIX_URL + '?' + MULTIPLE_VALUE)) {
            println(it)
            it.date != null
            it.fn == '7380440903639523'
            it.fp == '2358095958'
            it.sum == 23940
            it.doc == '12643'
            it.type == FiscalQr.Type.SELL
        }
    }
}
