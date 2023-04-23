package com.eslibs.common.file

import spock.lang.Specification

class FileResourceSpec extends Specification {

    def "GetFullPath (windows, internal)"() {
        expect:
        FileResource.create(true, false, "/path").fullPath == 'jar:/path'
    }

    def "GetFullPath (windows, external)"() {
        expect:
        FileResource.create(true, true, "/path").fullPath == 'file:////path'
    }

    def "GetFullPath (not windows, internal)"() {
        expect:
        FileResource.create(false, false, "/path").fullPath == 'jar:/path'
    }

    def "GetFullPath (not windows, external)"() {
        expect:
        FileResource.create(false, true, "/path").fullPath == 'file:/path'
    }
}
