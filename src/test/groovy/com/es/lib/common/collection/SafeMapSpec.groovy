package com.es.lib.common.collection

import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 12.04.2018
 */
class SafeMapSpec extends Specification {

    @Shared
    def safeMap = new SafeMap(['SHORT': '100', 'INT': '50000', 'LONG': '1231231231231231231', 'DOUBLE': '123.123', 'DATE1': '12.03.2018', 'DATE2': '2018-03-12'])

    def "GetShort"() {
        expect:
        safeMap.getShort('SHORT').get() == 100
        !safeMap.getShort('INT').isPresent()
        !safeMap.getShort('LONG').isPresent()
        !safeMap.getShort('DOUBLE').isPresent()
        !safeMap.getShort('DATE1').isPresent()
        !safeMap.getShort('DATE2').isPresent()
    }

    def "GetInt"() {
        expect:
        safeMap.getInt('SHORT').get() == 100
        safeMap.getInt('INT').get() == 50000
        !safeMap.getInt('LONG').isPresent()
        !safeMap.getInt('DOUBLE').isPresent()
        !safeMap.getInt('DATE1').isPresent()
        !safeMap.getInt('DATE2').isPresent()
    }

    def "GetLong"() {
        expect:
        safeMap.getLong('SHORT').get() == 100
        safeMap.getLong('INT').get() == 50000
        safeMap.getLong('LONG').get() == 1231231231231231231
        !safeMap.getLong('DOUBLE').isPresent()
        !safeMap.getLong('DATE1').isPresent()
        !safeMap.getLong('DATE2').isPresent()
    }

    def "GetDouble"() {
        expect:
        safeMap.getDouble('SHORT').get() == 100
        safeMap.getDouble('INT').get() == 50000
        safeMap.getDouble('LONG').get() == 1231231231231231231
        safeMap.getDouble('DOUBLE').get() == 123.123
        !safeMap.getDouble('DATE1').isPresent()
        !safeMap.getDouble('DATE2').isPresent()
    }

    def "GetDate"() {
        expect:
        !safeMap.getDate('SHORT').isPresent()
        !safeMap.getDate('INT').isPresent()
        !safeMap.getDate('LONG').isPresent()
        !safeMap.getDate('DOUBLE').isPresent()
        safeMap.getDate('DATE1').get().getDate() == 12
        safeMap.getDate('DATE1').get().getMonth() == 2
        safeMap.getDate('DATE1').get().getYear() == 118
        !safeMap.getDate('DATE2').isPresent()
        safeMap.getDate('DATE2', 'yyyy-MM-dd').get().getDate() == 12
        safeMap.getDate('DATE2', 'yyyy-MM-dd').get().getMonth() == 2
        safeMap.getDate('DATE2', 'yyyy-MM-dd').get().getYear() == 118
    }
}
