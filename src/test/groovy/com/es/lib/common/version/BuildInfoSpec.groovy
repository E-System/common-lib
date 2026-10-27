package com.es.lib.common.version

import spock.lang.Specification

class BuildInfoSpec extends Specification {

    def "BuildInfo getters, setters, hash and asMap"() {
        given:
        BuildInfo info = new BuildInfo("common-lib", "1.0.0", "2026-10-27")

        expect:
        info.name == "common-lib"
        info.version == "1.0.0"
        info.date == "2026-10-27"
        info.hash != null
        info.asMap() == [
                "name"   : "common-lib",
                "version": "1.0.0",
                "date"   : "2026-10-27",
                "hash"   : info.hash
        ]
        info.toString().contains("common-lib")

        when:
        info.setName("new-name")
        info.setVersion("2.0.0")
        info.setDate("2026-10-28")

        then:
        info.name == "new-name"
        info.version == "2.0.0"
        info.date == "2026-10-28"
        info.hash != null
    }

    def "VersionLoader readBuildInfo with custom supplier"() {
        given:
        String propsContent = "name=my-app\nversion=3.1.4\ndate=2026-01-01\n"

        when:
        BuildInfo info = VersionLoader.readBuildInfo { new ByteArrayInputStream(propsContent.bytes) }

        then:
        info.name == "my-app"
        info.version == "3.1.4"
        info.date == "2026-01-01"

        when:
        BuildInfo fallback = VersionLoader.readBuildInfo { null }

        then:
        fallback.name == VersionLoader.UNDEFINED
        fallback.version == VersionLoader.UNDEFINED_VERSION
        fallback.date == VersionLoader.UNDEFINED
    }

    def "VersionLoader readVersionAndModules and loadVersions"() {
        given:
        String mainProps = "version=1.0.0\nmodules=mod1,mod2\n"
        String modProps = "version=1.0.1\n"

        when:
        def entry = VersionLoader.readVersionAndModules { new ByteArrayInputStream(mainProps.bytes) }

        then:
        entry.key == "1.0.0"
        entry.value == ["mod1", "mod2"]

        when:
        VersionLoader.loadVersions(
                { new ByteArrayInputStream(mainProps.bytes) },
                { new ByteArrayInputStream(modProps.bytes) }
        )

        then:
        VersionLoader.VERSIONS["core"] == "1.0.0"
        VersionLoader.VERSIONS["mod1"] == "1.0.1"
        VersionLoader.VERSIONS["mod2"] == "1.0.1"
    }

    def "VersionLoader readBuildInfo default"() {
        when:
        BuildInfo info = VersionLoader.readBuildInfo()

        then:
        info != null
    }
}
