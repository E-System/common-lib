package com.es.lib.common.file

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

    def "Name and ext"(){
        when:
        def res = FileName.create('fileName', 'txt')
        then:
        res.name == 'fileName'
        res.ext == 'txt'
    }
}
