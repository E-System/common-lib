package com.es.lib.common.model

import spock.lang.Specification

class FiscalQrSpec extends Specification {

    def "Parse"() {
        expect:
        FiscalQr.of(null) == null
        FiscalQr.of('') == null
        with(FiscalQr.of('t=20260607T1303&s=1506.00&fn=7384440900865967&i=1323&fp=2179972036&n=1')) {
            println(it)
            it.date != null
            it.fn == '7384440900865967'
            it.fp == '2179972036'
            it.sum == 150600
            it.doc == '1323'
            it.type == FiscalQr.Type.SELL
        }
        with(FiscalQr.of('https://asdasdasd.ru?t=20260607T1303&s=1506.00&fn=7384440900865967&i=1323&fp=2179972036&n=1')) {
            println(it)
            it.date != null
            it.fn == '7384440900865967'
            it.fp == '2179972036'
            it.sum == 150600
            it.doc == '1323'
            it.type == FiscalQr.Type.SELL
        }
    }
}
