/*
 * Copyright (c) Extended System - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Extended System team (https://ext-system.com), 2016
 */

package com.es.lib.common.os

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 28.05.15
 */
class OSUtilSpec extends Specification {

    def "Должны быть корректно получены типы операционки"() {
        expect:
        OSUtil.getOS('win') == OSUtil.OS.WINDOWS
        OSUtil.getOS('Linux') == OSUtil.OS.LINUX
        OSUtil.getOS('Unix') == OSUtil.OS.LINUX
        OSUtil.getOS('mac') == OSUtil.OS.MACOS
        OSUtil.getOS('sunos') == OSUtil.OS.SOLARIS
    }

    def "Get os"(){
        expect:
        OSUtil.getOS() == OSUtil.getOS(System.getProperty("os.name").toLowerCase())
    }

    def "getAppConfigPath"() {
        expect:
        def path = OSUtil.getAppConfigPath("test")
        println(path)
        path != null
    }

    def "getConfigFilePath"() {
        expect:
        def path = OSUtil.getConfigFilePath("test", "config.dat")
        println(path)
        path != null
    }

    def "getAppPath"() {
        expect:
        def path = OSUtil.getAppPath()
        println(path)
        path != null
    }
}
