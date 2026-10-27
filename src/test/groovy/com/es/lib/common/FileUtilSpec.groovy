package com.es.lib.common

import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class FileUtilSpec extends Specification {

    def "Calculate CRC32 for String and bytes"() {
        given:
        String text = "test string for crc32"
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8)

        when:
        long crcFromString = FileUtil.crc32(text)
        long crcFromBytes = FileUtil.crc32(bytes)

        then:
        crcFromString != 0
        crcFromString == crcFromBytes
    }

    def "Read CRC32 from Stream, Path, File and String path"() {
        given:
        String content = "Hello world FileUtil test"
        Path tempFile = Files.createTempFile("fileutil_test", ".txt")
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8))

        when:
        def entryFromPath = FileUtil.readCrc32(tempFile)
        def entryFromFile = FileUtil.readCrc32(tempFile.toFile())
        def entryFromString = FileUtil.readCrc32(tempFile.toString())
        def entryFromStream = FileUtil.readCrc32(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)))

        then:
        entryFromPath.value == entryFromFile.value
        entryFromFile.value == entryFromString.value
        entryFromString.value == entryFromStream.value
        entryFromPath.value == FileUtil.crc32(content)

        cleanup:
        Files.deleteIfExists(tempFile)
    }

    def "Copy with CRC32 to File and Path"() {
        given:
        byte[] bytes = "Copy with crc32 test data".getBytes(StandardCharsets.UTF_8)
        Path destPath = Files.createTempFile("copy_dest", ".txt")
        Path destFile = Files.createTempFile("copy_dest_file", ".txt")

        when:
        long crc1 = FileUtil.copyWithCrc32(new ByteArrayInputStream(bytes), destPath)
        long crc2 = FileUtil.copyWithCrc32(new ByteArrayInputStream(bytes), destFile.toFile())

        then:
        crc1 == FileUtil.crc32(bytes)
        crc2 == crc1
        Files.readAllBytes(destPath) == bytes
        Files.readAllBytes(destFile) == bytes

        cleanup:
        Files.deleteIfExists(destPath)
        Files.deleteIfExists(destFile)
    }

    def "Read methods"() {
        given:
        String original = "First line\nSecond line\n"
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8)

        when:
        String readSimple = FileUtil.read(new ByteArrayInputStream(bytes), "UTF-8")
        String readBufDefault = FileUtil.readBuffered(new ByteArrayInputStream(bytes))
        String readBufEncoding = FileUtil.readBuffered(new ByteArrayInputStream(bytes), "UTF-8")
        byte[] readBytes = FileUtil.readBufferedByte(new ByteArrayInputStream(bytes))

        then:
        readSimple == original
        readBufDefault == "First lineSecond line"
        readBufEncoding == "First lineSecond line"
        readBytes == bytes
    }
}
