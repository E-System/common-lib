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

package com.es.lib.common.os

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 28.05.15
 */
class OSUtilSpec extends Specification {

    def "Success get OS type"() {
        expect:
        OSUtil.getOS('win') == OSUtil.OS.WINDOWS
        OSUtil.getOS('Linux') == OSUtil.OS.LINUX
        OSUtil.getOS('Unix') == OSUtil.OS.LINUX
        OSUtil.getOS('mac') == OSUtil.OS.MACOS
        OSUtil.getOS('sunos') == OSUtil.OS.SOLARIS
    }

    def "Get os name"() {
        when:
        def name = OSUtil.osName
        println(name)
        then:
        name != null
    }

    def "Get os version"() {
        when:
        def name = OSUtil.osVersion
        println(name)
        then:
        name != null
    }

    def "Get full os name with version"() {
        when:
        def name = OSUtil.fullOsName
        println(name)
        then:
        name == OSUtil.osName + ' ' + OSUtil.osVersion
    }

    def "Get os"() {
        expect:
        OSUtil.getOS() == OSUtil.getOS(System.getProperty("os.name").toLowerCase())
    }

    def "getAppConfigPath"() {
        expect:
        def path = OSUtil.getAppConfigPath("es", "test")
        println(path)
        path != null
    }

    def "getConfigFilePath"() {
        expect:
        def path = OSUtil.getConfigFilePath("es", "test", "config.dat")
        println(path)
        path != null
    }

    def "getAppPath"() {
        expect:
        def path = OSUtil.appPath
        println(path)
        path != null
    }
}
