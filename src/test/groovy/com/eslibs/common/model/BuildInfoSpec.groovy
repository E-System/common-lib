package com.eslibs.common.model

import com.eslibs.common.reflection.Reflects
import spock.lang.Specification

class BuildInfoSpec extends Specification {

    def "ReadBuildInfo with supplier"() {
        when:
        def info = BuildInfo.create({
            return BuildInfo.class.getResourceAsStream("/com/eslibs/test-build.properties")
        })
        then:
        info != null
        info.name == "common-lib"
        info.version == "1.5.0-SNAPSHOT"
        info.date == "2018-01-31T16:00:45.167"
    }

    def "List all by prefix"() {
        when:
        println Reflects.getResources("com.eslibs", v -> v.endsWith("build.properties"))
        def items = BuildInfo.list("com.eslibs")
        println items
        then:
        !items.empty
    }
}
