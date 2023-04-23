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

package com.eslibs.common

import com.eslibs.common.Net
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 05.03.15
 */
class NetSpec extends Specification {

    def "Convert string ip to long"() {
        expect:
        Net.ip4ToLong(ip) == result
        where:
        ip                || result
        '176.50.191.18'   || 2956115730
        '116.10.11.18'    || 1946815250
        '16.110.110.118'  || 275672694
        '255.255.255.255' || 4294967295
    }

    def "Convert numeric ip to string"() {
        expect:
        Net.longToIp4(ip) == result
        where:
        ip         || result
        2956115730 || '176.50.191.18'
        1946815250 || '116.10.11.18'
        275672694  || '16.110.110.118'
        4294967295 || '255.255.255.255'
    }

    def "Get ip from subnet"() {
        expect:
        Net.getNetwork(ip) == result
        where:
        ip                   || result
        '176.50.191.18/24'   || 2956115730
        '116.10.11.18/22'    || 1946815250
        '16.110.110.118/22'  || 275672694
        '255.255.255.255/24' || 4294967295
    }

    def "Get NPE for null subnet"() {
        when:
        Net.getNetwork(null)
        then:
        thrown NullPointerException
    }

    def "Get IllegalArgumentException for subnet without / symbol"() {
        when:
        Net.getNetwork("123.123.123.123")
        then:
        thrown IllegalArgumentException
    }


    def "get broadcast address from subnet"() {
        expect:
        Net.getBroadcast(ip) == result
        where:
        ip                || result
        '176.50.191.0/24' || 2956115967
        '17.53.8.0/22'    || 288689151

    }

    def "Get NullPointerException for null broadcast address"() {
        when:
        Net.getBroadcast(null)
        then:
        thrown NullPointerException
    }

    def "Get IllegalArgumentException for broadcast address without /"() {
        when:
        Net.getBroadcast("123.123.123.123")
        then:
        thrown IllegalArgumentException
    }

    def "Address in range"() {
        expect:
        Net.inRange("10.0.0.0", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.1.0.0", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.1.2.0", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.1.2.3", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.255.255.255", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.255.255.254", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.255.254.255", "10.0.0.0", "10.255.255.255")
        Net.inRange("10.254.255.255", "10.0.0.0", "10.255.255.255")
    }

    def "Address not in range"() {
        expect:
        !Net.inRange("9.0.0.0", "10.0.0.0", "10.255.255.255")
        !Net.inRange("9.1.0.0", "10.0.0.0", "10.255.255.255")
        !Net.inRange("9.1.2.0", "10.0.0.0", "10.255.255.255")
        !Net.inRange("9.1.2.3", "10.0.0.0", "10.255.255.255")
    }

    def "Address in local address range"() {
        expect:
        Net.isLocalNetwork("10.0.0.0")
        Net.isLocalNetwork("10.1.0.0")
        Net.isLocalNetwork("10.1.2.0")
        Net.isLocalNetwork("10.1.2.3")
        Net.isLocalNetwork("10.255.255.255")
        Net.isLocalNetwork("10.255.255.255")
        Net.isLocalNetwork("10.255.255.254")
        Net.isLocalNetwork("10.255.254.255")
        Net.isLocalNetwork("10.254.255.255")

        Net.isLocalNetwork("172.16.0.0")
        Net.isLocalNetwork("172.16.1.0")
        Net.isLocalNetwork("172.16.1.2")

        Net.isLocalNetwork("172.31.255.255")
        Net.isLocalNetwork("172.31.255.254")
        Net.isLocalNetwork("172.31.254.255")
        Net.isLocalNetwork("172.30.255.255")

        Net.isLocalNetwork("192.168.0.0")
        Net.isLocalNetwork("192.168.1.0")
        Net.isLocalNetwork("192.168.1.2")

        Net.isLocalNetwork("192.168.255.255")
        Net.isLocalNetwork("192.168.254.255")
        Net.isLocalNetwork("192.168.255.254")
    }

    def "Address not in local address range"() {
        expect:
        !Net.isLocalNetwork("9.0.0.0")
        !Net.isLocalNetwork("9.1.0.0")
        !Net.isLocalNetwork("9.1.2.0")
        !Net.isLocalNetwork("9.1.2.3")
        !Net.isLocalNetwork("11.255.255.255")
        !Net.isLocalNetwork("11.255.255.255")
        !Net.isLocalNetwork("11.255.255.254")
        !Net.isLocalNetwork("11.255.254.255")
        !Net.isLocalNetwork("11.254.255.255")

        !Net.isLocalNetwork("171.16.0.0")
        !Net.isLocalNetwork("171.16.1.0")
        !Net.isLocalNetwork("171.16.1.2")

        !Net.isLocalNetwork("173.31.255.255")
        !Net.isLocalNetwork("173.31.255.254")
        !Net.isLocalNetwork("173.31.254.255")
        !Net.isLocalNetwork("173.30.255.255")

        !Net.isLocalNetwork("191.168.0.0")
        !Net.isLocalNetwork("191.168.1.0")
        !Net.isLocalNetwork("191.168.1.2")

        !Net.isLocalNetwork("193.168.255.255")
        !Net.isLocalNetwork("193.168.254.255")
        !Net.isLocalNetwork("193.168.255.254")
    }

    def "Matches"(){
        expect:
        Net.matches("192.168.2.1", "192.168.2.1")
        !Net.matches("192.168.2.1", "192.168.2.0/32")
        Net.matches("192.168.2.5", "192.168.2.0/24")
        !Net.matches("92.168.2.1", "fe80:0:0:0:0:0:c0a8:1/120")
        Net.matches("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/120")
        !Net.matches("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/128")
        !Net.matches("fe80:0:0:0:0:0:c0a8:11", "192.168.2.0/32")

    }
}

