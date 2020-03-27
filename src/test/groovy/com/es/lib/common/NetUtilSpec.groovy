/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class NetUtilSpec extends Specification {

    def "Конвертировать строковое представление ip в числовое"() {
        expect:
        NetUtil.ip4ToLong(ip) == result
        where:
        ip                || result
        '176.50.191.18'   || 2956115730
        '116.10.11.18'    || 1946815250
        '16.110.110.118'  || 275672694
        '255.255.255.255' || 4294967295
    }

    def "Конвертировать числовое представление ip в строковое"() {
        expect:
        NetUtil.longToIp4(ip) == result
        where:
        ip         || result
        2956115730 || '176.50.191.18'
        1946815250 || '116.10.11.18'
        275672694  || '16.110.110.118'
        4294967295 || '255.255.255.255'
    }

    def "Получить ip из подсети"() {
        expect:
        NetUtil.getNetwork(ip) == result
        where:
        ip                   || result
        '176.50.191.18/24'   || 2956115730
        '116.10.11.18/22'    || 1946815250
        '16.110.110.118/22'  || 275672694
        '255.255.255.255/24' || 4294967295
    }

    def "Должны получить NPE для null адреса подсети"() {
        when:
        NetUtil.getNetwork(null)
        then:
        thrown NullPointerException
    }

    def "Должны получить IAE для адреса подсети без /"() {
        when:
        NetUtil.getNetwork("123.123.123.123")
        then:
        thrown IllegalArgumentException
    }


    def "Получить широковещательный адрес для подсети"() {
        expect:
        NetUtil.getBroadcast(ip) == result
        where:
        ip                || result
        '176.50.191.0/24' || 2956115967
        '17.53.8.0/22'    || 288689151

    }

    def "Должны получить NPE для null широковещательного адреса"() {
        when:
        NetUtil.getBroadcast(null)
        then:
        thrown NullPointerException
    }

    def "Должны получить IAE для широковещательного адреса без /"() {
        when:
        NetUtil.getBroadcast("123.123.123.123")
        then:
        thrown IllegalArgumentException
    }

    def "Адрес должен входить в интервал"() {
        expect:
        NetUtil.inRange("10.0.0.0", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.1.0.0", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.1.2.0", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.1.2.3", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.255.255.255", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.255.255.254", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.255.254.255", "10.0.0.0", "10.255.255.255")
        NetUtil.inRange("10.254.255.255", "10.0.0.0", "10.255.255.255")
    }

    def "Адрес должен быть за пределами интервала"() {
        expect:
        !NetUtil.inRange("9.0.0.0", "10.0.0.0", "10.255.255.255")
        !NetUtil.inRange("9.1.0.0", "10.0.0.0", "10.255.255.255")
        !NetUtil.inRange("9.1.2.0", "10.0.0.0", "10.255.255.255")
        !NetUtil.inRange("9.1.2.3", "10.0.0.0", "10.255.255.255")
    }

    def "Адрес должен принадлежать локальной сети"() {
        expect:
        NetUtil.isLocalNetwork("10.0.0.0")
        NetUtil.isLocalNetwork("10.1.0.0")
        NetUtil.isLocalNetwork("10.1.2.0")
        NetUtil.isLocalNetwork("10.1.2.3")
        NetUtil.isLocalNetwork("10.255.255.255")
        NetUtil.isLocalNetwork("10.255.255.255")
        NetUtil.isLocalNetwork("10.255.255.254")
        NetUtil.isLocalNetwork("10.255.254.255")
        NetUtil.isLocalNetwork("10.254.255.255")

        NetUtil.isLocalNetwork("172.16.0.0")
        NetUtil.isLocalNetwork("172.16.1.0")
        NetUtil.isLocalNetwork("172.16.1.2")

        NetUtil.isLocalNetwork("172.31.255.255")
        NetUtil.isLocalNetwork("172.31.255.254")
        NetUtil.isLocalNetwork("172.31.254.255")
        NetUtil.isLocalNetwork("172.30.255.255")

        NetUtil.isLocalNetwork("192.168.0.0")
        NetUtil.isLocalNetwork("192.168.1.0")
        NetUtil.isLocalNetwork("192.168.1.2")

        NetUtil.isLocalNetwork("192.168.255.255")
        NetUtil.isLocalNetwork("192.168.254.255")
        NetUtil.isLocalNetwork("192.168.255.254")
    }

    def "Адрес не должен принадлежать локальной сети"() {
        expect:
        !NetUtil.isLocalNetwork("9.0.0.0")
        !NetUtil.isLocalNetwork("9.1.0.0")
        !NetUtil.isLocalNetwork("9.1.2.0")
        !NetUtil.isLocalNetwork("9.1.2.3")
        !NetUtil.isLocalNetwork("11.255.255.255")
        !NetUtil.isLocalNetwork("11.255.255.255")
        !NetUtil.isLocalNetwork("11.255.255.254")
        !NetUtil.isLocalNetwork("11.255.254.255")
        !NetUtil.isLocalNetwork("11.254.255.255")

        !NetUtil.isLocalNetwork("171.16.0.0")
        !NetUtil.isLocalNetwork("171.16.1.0")
        !NetUtil.isLocalNetwork("171.16.1.2")

        !NetUtil.isLocalNetwork("173.31.255.255")
        !NetUtil.isLocalNetwork("173.31.255.254")
        !NetUtil.isLocalNetwork("173.31.254.255")
        !NetUtil.isLocalNetwork("173.30.255.255")

        !NetUtil.isLocalNetwork("191.168.0.0")
        !NetUtil.isLocalNetwork("191.168.1.0")
        !NetUtil.isLocalNetwork("191.168.1.2")

        !NetUtil.isLocalNetwork("193.168.255.255")
        !NetUtil.isLocalNetwork("193.168.254.255")
        !NetUtil.isLocalNetwork("193.168.255.254")
    }

    def "Matches"(){
        expect:
        NetUtil.matches("192.168.2.1", "192.168.2.1")
        !NetUtil.matches("192.168.2.1", "192.168.2.0/32")
        NetUtil.matches("192.168.2.5", "192.168.2.0/24")
        !NetUtil.matches("92.168.2.1", "fe80:0:0:0:0:0:c0a8:1/120")
        NetUtil.matches("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/120")
        !NetUtil.matches("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/128")
        !NetUtil.matches("fe80:0:0:0:0:0:c0a8:11", "192.168.2.0/32")

    }
}

