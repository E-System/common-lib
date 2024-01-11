package com.eslibs.common.file

import spock.lang.Specification

import java.nio.file.Paths

class FileNameSpec extends Specification {

    def "Only name"() {
        when:
        def res = FileName.of('fileName')
        then:
        res.name() == 'fileName'
        res.ext() == ''
    }

    def "Only name with path"() {
        when:
        def res = FileName.of('/tmp/fileName')
        then:
        res.name() == 'fileName'
        res.ext() == ''
    }

    def "Name and extension"() {
        when:
        def res = FileName.of('fileName.txt')
        then:
        res.name() == 'fileName'
        res.ext() == 'txt'
    }

    def "Name and extension"() {
        expect:
        FileName.of('fileName.txt').ext() == 'txt'
        FileName.of('fileName.Txt').ext() == 'txt'
        FileName.of('fileName.TXT').ext() == 'txt'
        FileName.of('fileName.Txt', false).ext() == 'Txt'
        FileName.of('fileName.TXT', false).ext() == 'TXT'
    }

    def "Name and extension with path"() {
        when:
        def res = FileName.of('/tmp/fileName.txt')
        then:
        res.name() == 'fileName'
        res.ext() == 'txt'
    }

    def "Name and extension from path"() {
        when:
        def res = FileName.of(Paths.get('/tmp/fileName.txt'))
        then:
        res.name() == 'fileName'
        res.ext() == 'txt'
    }

    def "Name and ext"() {
        when:
        def res = FileName.of('fileName', 'txt')
        then:
        res.name() == 'fileName'
        res.ext() == 'txt'
    }

    def "FullName"() {
        expect:
        FileName.full('name', 'ext') == 'name.ext'
    }

    def "AbbreviatedFileName"() {
        expect:
        FileName.abbreviated('name', 'ext', 10) == 'name.ext'
        FileName.abbreviated('name-name-name-name', 'ext', 10) == 'na..me.ext'
    }
}
