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

import com.eslibs.common.Sys
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 28.05.15
 */
class SysSpec extends Specification {

    def "Correct os types recognized"() {
        expect:
        Sys.getOS('win') == Sys.OS.WINDOWS
        Sys.getOS('Linux') == Sys.OS.LINUX
        Sys.getOS('Unix') == Sys.OS.LINUX
        Sys.getOS('mac') == Sys.OS.MACOS
        Sys.getOS('sunos') == Sys.OS.SOLARIS
    }

    def "Get os"() {
        expect:
        Sys.getOS() == Sys.getOS(System.getProperty("os.name").toLowerCase())
    }

    def "getAppConfigPath"() {
        expect:
        def path = Sys.getAppConfigPath("es", "test")
        println(path)
        path != null
    }

    def "getConfigFilePath"() {
        expect:
        def path = Sys.getConfigFilePath("es", "test", "config.dat")
        println(path)
        path != null
    }

    def "getAppPath"() {
        expect:
        def path = Sys.getAppPath()
        println(path)
        path != null
    }
}
