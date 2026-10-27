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
        given:
        def tempFile = java.nio.file.Files.createTempFile("outdata", ".txt")
        java.nio.file.Files.write(tempFile, BYTE_DATA)

        when:
        def data = (FileData) OutputData.create(FILE_NAME, tempFile.parent.toString(), tempFile.fileName)
        then:
        !data.stream
        !data.bytes
        data.isFile()
        data.relativePath == tempFile.parent.toString()
        data.fileName == FILE_NAME
        data.content == tempFile.fileName
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
        data.asBytes() == BYTE_DATA

        cleanup:
        java.nio.file.Files.deleteIfExists(tempFile)
    }

    def "Create with file without relative path"() {
        given:
        def tempFile = java.nio.file.Files.createTempFile("outdata2", ".txt")
        java.nio.file.Files.write(tempFile, BYTE_DATA)

        when:
        def data = (FileData) OutputData.create(FILE_NAME, null, tempFile)
        then:
        data.isFile()
        data.asBytes() == BYTE_DATA

        cleanup:
        java.nio.file.Files.deleteIfExists(tempFile)
    }

    def "Create with byte array"() {
        when:
        def data = (ByteData) OutputData.create(FILE_NAME, CONTENT_TYPE, BYTE_DATA)
        then:
        !data.stream
        data.bytes
        !data.isFile()
        data.contentType == CONTENT_TYPE
        data.fileName == FILE_NAME
        data.content == BYTE_DATA
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
        data.asBytes() == BYTE_DATA
    }

    def "Create with input stream"() {
        when:
        def data = (StreamData) OutputData.create(FILE_NAME, CONTENT_TYPE, new ByteArrayInputStream(BYTE_DATA))
        then:
        data.stream
        !data.bytes
        !data.isFile()
        data.contentType == CONTENT_TYPE
        data.fileName == FILE_NAME
        data.content != null
        with(data.info()){
            it.fileName == FILE_NAME
            it.contentType == 'text/plain'
        }
        data.asBytes() == BYTE_DATA
    }

    def "Custom OutputData throws on info and asBytes"() {
        given:
        def custom = new OutputData("custom.txt") {}

        when:
        custom.info()
        then:
        thrown(IllegalArgumentException)

        when:
        custom.asBytes()
        then:
        thrown(IllegalArgumentException)
    }
}
