package com.es.lib.common.model

import com.es.lib.common.reflection.Reflects
import spock.lang.Specification

import java.util.function.Supplier

class BuildInfoSpec extends Specification {

    def "ReadBuildInfo with supplier"() {
        when:
        def info = BuildInfo.create(new Supplier<InputStream>() {
            @Override
            InputStream get() {
                return BuildInfo.class.getResourceAsStream("/com/es/test-build.properties")
            }
        })
        then:
        info != null
        info.name == "common-lib"
        info.version == "1.5.0-SNAPSHOT"
        info.date == "2018-01-31T16:00:45.167"
    }

    def "List all by prefix"() {
        when:
        println Reflects.getResources("com.es", v -> v.endsWith("build.properties"))
        def items = BuildInfo.list("com.es")
        println items
        then:
        !items.empty
    }
}
