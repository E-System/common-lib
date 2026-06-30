package com.es.lib.common.file.output

import spock.lang.Specification

import java.nio.file.Paths

class OutputDataSpec extends Specification {

    static FILE_NAME = "filename.txt"
    static RELATIVE_PATH = "/2020/01"
    static CONTENT_TYPE = "text/plain"
    static BYTE_DATA = new byte[] { 0x01 }
    static STREAM_DATA = new ByteArrayInputStream(BYTE_DATA)

    def "Create with file"() {
        when:
        def data = (FileData) OutputData.create(FILE_NAME, RELATIVE_PATH, Paths.get(RELATIVE_PATH))
        then:
        !data.stream
        !data.bytes
        data.relativePath == RELATIVE_PATH
        data.fileName == FILE_NAME
        data.content == Paths.get(RELATIVE_PATH)
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
    }

    def "Create with byte array"() {
        when:
        def data = (ByteData) OutputData.create(FILE_NAME, CONTENT_TYPE, BYTE_DATA)
        then:
        !data.stream
        data.bytes
        data.contentType == CONTENT_TYPE
        data.fileName == FILE_NAME
        data.content == BYTE_DATA
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
    }

    def "Create with input stream"() {
        when:
        def data = (StreamData) OutputData.create(FILE_NAME, CONTENT_TYPE, STREAM_DATA)
        then:
        data.stream
        !data.bytes
        data.contentType == CONTENT_TYPE
        data.fileName == FILE_NAME
        data.content == STREAM_DATA
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
    }
}
