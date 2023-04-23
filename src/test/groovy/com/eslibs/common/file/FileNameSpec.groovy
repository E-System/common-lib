package com.eslibs.common.file

import spock.lang.Specification

import java.nio.file.Paths

class FileNameSpec extends Specification {

    def "Only name"() {
        when:
        def res = FileName.create('fileName')
        then:
        res.name == 'fileName'
        res.ext == ''
    }

    def "Only name with path"() {
        when:
        def res = FileName.create('/tmp/fileName')
        then:
        res.name == 'fileName'
        res.ext == ''
    }

    def "Name and extension"() {
        when:
        def res = FileName.create('fileName.txt')
        then:
        res.name == 'fileName'
        res.ext == 'txt'
    }

    def "Name and extension"() {
        expect:
        FileName.create('fileName.txt').ext == 'txt'
        FileName.create('fileName.Txt').ext == 'txt'
        FileName.create('fileName.TXT').ext == 'txt'
        FileName.create('fileName.Txt', false).ext == 'Txt'
        FileName.create('fileName.TXT', false).ext == 'TXT'
    }

    def "Name and extension with path"() {
        when:
        def res = FileName.create('/tmp/fileName.txt')
        then:
        res.name == 'fileName'
        res.ext == 'txt'
    }

    def "Name and extension from path"() {
        when:
        def res = FileName.create(Paths.get('/tmp/fileName.txt'))
        then:
        res.name == 'fileName'
        res.ext == 'txt'
    }

    def "Name and ext"() {
        when:
        def res = FileName.create('fileName', 'txt')
        then:
        res.name == 'fileName'
        res.ext == 'txt'
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
