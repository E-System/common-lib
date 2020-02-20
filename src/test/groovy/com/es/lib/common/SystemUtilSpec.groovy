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
 * @since 28.05.15
 */
class SystemUtilSpec extends Specification {

    def "Должны быть корректно получены типы операционки"() {
        expect:
        SystemUtil.getOS('win') == SystemUtil.OS.WINDOWS
        SystemUtil.getOS('Linux') == SystemUtil.OS.LINUX
        SystemUtil.getOS('Unix') == SystemUtil.OS.LINUX
        SystemUtil.getOS('mac') == SystemUtil.OS.MACOS
        SystemUtil.getOS('sunos') == SystemUtil.OS.SOLARIS
    }

    def "Get os"() {
        expect:
        SystemUtil.getOS() == SystemUtil.getOS(System.getProperty("os.name").toLowerCase())
    }

    def "getAppConfigPath"() {
        expect:
        def path = SystemUtil.getAppConfigPath("es", "test")
        println(path)
        path != null
    }

    def "getConfigFilePath"() {
        expect:
        def path = SystemUtil.getConfigFilePath("es", "test", "config.dat")
        println(path)
        path != null
    }

    def "getAppPath"() {
        expect:
        def path = SystemUtil.getAppPath()
        println(path)
        path != null
    }
}
