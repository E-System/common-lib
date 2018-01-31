package com.es.lib.common.version

import spock.lang.Specification

import java.util.function.Supplier

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 29.06.17
 */
class VersionLoaderSpec extends Specification {

    def "ReadBuildInfo with supplier"() {
        when:
        def info = VersionLoader.readBuildInfo(new Supplier<InputStream>() {
            @Override
            InputStream get() {
                return VersionLoader.class.getResourceAsStream("/com/es/test-build.properties")
            }
        })
        then:
        info != null
        info.name == "common-lib"
        info.version == "1.3.0-SNAPSHOT"
        info.date == "2018-01-31T16:00:45.167"
    }

    def "ReadBuildInfo"() {
        when:
        def info = VersionLoader.readBuildInfo()
        then:
        info != null
        info.name == "common-lib-native"
        info.version == "1.3.0-SNAPSHOT"
        info.date == "2018-01-31T16:00:45.167"
    }

}
