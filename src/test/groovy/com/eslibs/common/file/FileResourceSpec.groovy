package com.eslibs.common.file

import spock.lang.Specification

import java.nio.file.Paths

class FileResourceSpec extends Specification {

    def "GetFullPath (windows, internal)"() {
        expect:
        FileResource.of(Paths.get('/path'), true, false).fullPath == 'jar:/path'
    }

    def "GetFullPath (windows, external)"() {
        expect:
        FileResource.of(Paths.get('/path'), true, true).fullPath == 'file:////path'
    }

    def "GetFullPath (not windows, internal)"() {
        expect:
        FileResource.of(Paths.get('/path'), false, false).fullPath == 'jar:/path'
    }

    def "GetFullPath (not windows, external)"() {
        expect:
        FileResource.of(Paths.get('/path'), false, true).fullPath == 'file:/path'
    }
}
