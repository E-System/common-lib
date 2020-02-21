/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.file

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 11.03.16
 */
class FileUtilSpec extends Specification {

    def "Copy with crc32"() {
        when:
        def file = Paths.get('/tmp/cwcr/copy_with_crc_test.txt')
        def res = FileUtil.copyWithCrc32(new ByteArrayInputStream('Hello'.bytes), file)
        then:
        Files.exists(file)
        new String(Files.readAllBytes(file)) == 'Hello'
        res == 4157704578
        FileUtil.delete(file)
    }

    def "Read with crc32"() {
        when:
        def file = Files.createTempFile("defined_file_from_spock_test", ".txt")
        Files.write(file, 'Hello'.bytes)
        def res = FileUtil.readCrc32(file)
        FileUtil.delete(file)
        then:
        res.key == 'Hello'
        res.value == 4157704578
    }

    def "Silent delete null file expect false"() {
        expect:
        FileUtil.delete(null)
    }

    def "Silent delete not exist file expect false"() {
        expect:
        FileUtil.delete(Paths.get('/tmp/undefined_file_from_spock_test.txt'))
    }

    def "Silent delete exist file expect true"() {
        when:
        def file = Files.createTempFile("defined_file_from_spock_test", ".txt")
        then:
        FileUtil.delete(file)
    }

    def "Get file type"() {
        expect:
        FileUtil.fileType(ext) == result
        where:
        ext                   | result
        null                  | FileType.OTHER
        "abc"                 | FileType.OTHER
        ""                    | FileType.OTHER
        "doc"                 | FileType.WORD
        "docx"                | FileType.WORD
        "rtf"                 | FileType.WORD
        "odt"                 | FileType.WORD
        "xls"                 | FileType.EXCEL
        "xlsx"                | FileType.EXCEL
        "pdf"                 | FileType.PDF
        "txt"                 | FileType.TEXT
        "zip"                 | FileType.ARCHIVE
        "rar"                 | FileType.ARCHIVE
        "bz2"                 | FileType.ARCHIVE
        "gz2"                 | FileType.ARCHIVE
        "png"                 | FileType.IMAGE
        "jpg"                 | FileType.IMAGE
        "jpeg"                | FileType.IMAGE
        "gif"                 | FileType.IMAGE
        "bmp"                 | FileType.IMAGE
        "xml"                 | FileType.CODE
        "csv"                 | FileType.CODE
        "java"                | FileType.CODE
        "cpp"                 | FileType.CODE
        "h"                   | FileType.CODE
        "avi"                 | FileType.VIDEO
        "mkv"                 | FileType.VIDEO
        "mov"                 | FileType.VIDEO
        "ppt"                 | FileType.POWERPOINT
        "pptx"                | FileType.POWERPOINT
        "pps"                 | FileType.POWERPOINT
        "ppsx"                | FileType.POWERPOINT
        "hello.ppsx"          | FileType.POWERPOINT
        "/tmp/hello.ppsx"     | FileType.POWERPOINT
        "/tmp/ppsx"           | FileType.POWERPOINT
        "/tmp/hello.asd.ppsx" | FileType.POWERPOINT
        "/tmp/"               | FileType.OTHER
    }


    def "Get icon"() {
        expect:
        FileUtil.fileType(ext).icon == icon
        where:
        ext    | icon
        null   | "file-o"
        "abc"  | "file-o"
        ""     | "file-o"

        "doc"  | "file-word-o"
        "docx" | "file-word-o"
        "rtf"  | "file-word-o"
        "odt"  | "file-word-o"

        "xls"  | "file-excel-o"
        "xlsx" | "file-excel-o"

        "pdf"  | "file-pdf-o"

        "txt"  | "file-text-o"

        "zip"  | "file-archive-o"
        "rar"  | "file-archive-o"
        "bz2"  | "file-archive-o"
        "gz2"  | "file-archive-o"

        "png"  | "file-image-o"
        "jpg"  | "file-image-o"
        "jpeg" | "file-image-o"
        "gif"  | "file-image-o"
        "bmp"  | "file-image-o"

        "xml"  | "file-code-o"
        "csv"  | "file-code-o"
        "java" | "file-code-o"
        "cpp"  | "file-code-o"
        "h"    | "file-code-o"
        "avi"  | "file-video-o"
        "mkv"  | "file-video-o"
        "mov"  | "file-video-o"
        "ppt"  | "file-powerpoint-o"
        "pptx" | "file-powerpoint-o"
        "pps"  | "file-powerpoint-o"
        "ppsx" | "file-powerpoint-o"
    }


    def "Get mime"() {
        expect:
        FileUtil.mime(fileName) == result
        where:
        fileName        || result
        ""              || "application/octet-stream"
        "file."         || "application/octet-stream"
        "file.js"       || "application/javascript"
        "file.png"      || "image/png"
        "file.Js"       || "application/javascript"
        "file.JS"       || "application/javascript"
        "file.bmp"      || "image/bmp"
        "file.woff"     || "font/woff"
        "file.woff2"    || "font/woff2"
        "file.ttf"      || "font/ttf"
        "file.tiff"     || "image/tiff"
        "file.css"      || "text/css"
        "file.jpeg"     || "image/jpeg"
        "file.jpg"      || "image/jpeg"
        "file.JPG"      || "image/jpeg"
        "file.xml"      || "application/xml"
        "file.json"     || "application/json"
        "file.zip"      || "application/zip"
        "file.tar"      || "application/x-tar"
        "file.rar"      || "application/x-rar-compressed"
        "file.tar.gz"   || "application/x-gzip"
        "file.tar.bz2"  || "application/x-bzip2"
        "file.pdf"      || "application/pdf"
        "file.xls"      || "application/vnd.ms-excel"
        "file.xlsx"     || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "file.doc"      || "application/msword"
        "file.docx"     || "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "file.ico"      || "image/x-icon"
        "file.ppt"      || "application/vnd.ms-powerpoint"
        "/tmp/file.ppt" || "application/vnd.ms-powerpoint"
        "/tmp/ppt"      || "application/vnd.ms-powerpoint"
        "/tmp/"         || "application/octet-stream"
        "file."         || "application/octet-stream"
        "js"            || "application/javascript"
        "png"           || "image/png"
        "Js"            || "application/javascript"
        "JS"            || "application/javascript"
        "bmp"           || "image/bmp"
        "woff"          || "font/woff"
        "woff2"         || "font/woff2"
        "ttf"           || "font/ttf"
        "tiff"          || "image/tiff"
        "css"           || "text/css"
        "jpeg"          || "image/jpeg"
        "jpg"           || "image/jpeg"
        "JPG"           || "image/jpeg"
        "xml"           || "application/xml"
        "json"          || "application/json"
        "zip"           || "application/zip"
        "tar"           || "application/x-tar"
        "rar"           || "application/x-rar-compressed"
        "gz"            || "application/x-gzip"
        "bz2"           || "application/x-bzip2"
        "pdf"           || "application/pdf"
        "xls"           || "application/vnd.ms-excel"
        "xlsx"          || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "doc"           || "application/msword"
        "docx"          || "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "ico"           || "image/x-icon"
        "ppt"           || "application/vnd.ms-powerpoint"
    }

    def "Content file name disposition"() {
        expect:
        FileUtil.fileNameDisposition(true, "Hello.txt") == "attachment; filename=\"Hello.txt\"; filename*=UTF-8''Hello.txt"
        FileUtil.fileNameDisposition(false, "Hello.txt") == "inline; filename=\"Hello.txt\"; filename*=UTF-8''Hello.txt"
        FileUtil.fileNameDisposition(true, "Тест.txt") == "attachment; filename=\"Тест.txt\"; filename*=UTF-8''%D0%A2%D0%B5%D1%81%D1%82.txt"
        FileUtil.fileNameDisposition(false, "Тест.txt") == "inline; filename=\"Тест.txt\"; filename*=UTF-8''%D0%A2%D0%B5%D1%81%D1%82.txt"
    }
}
