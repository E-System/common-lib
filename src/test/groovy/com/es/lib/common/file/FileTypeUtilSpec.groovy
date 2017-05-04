package com.es.lib.common.file

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 04.05.17
 */
class FileTypeUtilSpec extends Specification {

    def "IsWord"() {
        expect:
        FileTypeUtil.isWord(ext) == result
        where:
        ext    | result
        null   | false
        "abc"  | false
        ""     | false
        "doc"  | true
        "docx" | true
        "rtf"  | true
        "odt"  | true

    }

    def "IsExcel"() {
        expect:
        FileTypeUtil.isExcel(ext) == result
        where:
        ext    | result
        null   | false
        "abc"  | false
        ""     | false
        "xls"  | true
        "xlsx" | true
    }

    def "IsPdf"() {
        expect:
        FileTypeUtil.isPdf(ext) == result
        where:
        ext   | result
        null  | false
        "abc" | false
        ""    | false
        "pdf" | true
    }

    def "IsText"() {
        expect:
        FileTypeUtil.isText(ext) == result
        where:
        ext   | result
        null  | false
        "abc" | false
        ""    | false
        "txt" | true
    }

    def "IsArchive"() {
        expect:
        FileTypeUtil.isArchive(ext) == result
        where:
        ext   | result
        null  | false
        "abc" | false
        ""    | false
        "zip" | true
        "rar" | true
        "bz2" | true
        "gz2" | true
    }

    def "IsImage"() {
        expect:
        FileTypeUtil.isImage(ext) == result
        where:
        ext    | result
        null   | false
        "abc"  | false
        ""     | false
        "png"  | true
        "jpg"  | true
        "jpeg" | true
        "gif"  | true
        "bmp"  | true
    }

    def "IsOther"() {
        expect:
        FileTypeUtil.isOther(ext) == result
        where:
        ext    | result
        null   | true
        "abc"  | true
        ""     | true

        "doc"  | false
        "docx" | false
        "rtf"  | false
        "odt"  | false

        "xls"  | false
        "xlsx" | false

        "pdf"  | false

        "txt"  | false

        "zip"  | false
        "rar"  | false
        "bz2"  | false
        "gz2"  | false

        "png"  | false
        "jpg"  | false
        "jpeg" | false
        "gif"  | false
        "bmp"  | false
    }

    def "GetIconPostfix"() {
        expect:
        FileTypeUtil.getIconPostfix(ext) == postfix
        where:
        ext    | postfix
        null   | ""
        "abc"  | ""
        ""     | ""

        "doc"  | "-word"
        "docx" | "-word"
        "rtf"  | "-word"
        "odt"  | "-word"

        "xls"  | "-excel"
        "xlsx" | "-excel"

        "pdf"  | "-pdf"

        "txt"  | "-text"

        "zip"  | "-archive"
        "rar"  | "-archive"
        "bz2"  | "-archive"
        "gz2"  | "-archive"

        "png"  | "-image"
        "jpg"  | "-image"
        "jpeg" | "-image"
        "gif"  | "-image"
        "bmp"  | "-image"
    }
}
