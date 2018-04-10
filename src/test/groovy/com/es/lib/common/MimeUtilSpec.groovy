package com.es.lib.common

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.2018
 */
class MimeUtilSpec extends Specification {

    def "Get"() {
        expect:
        MimeUtil.get(fileName) == result
        where:
        fileName       || result
        ""             || "application/octet-stream"
        "file."        || "application/octet-stream"
        "file.js"      || "application/javascript"
        "file.png"     || "image/png"
        "file.Js"      || "application/javascript"
        "file.JS"      || "application/javascript"
        "file.bmp"     || "image/bmp"
        "file.woff"    || "font/woff"
        "file.woff2"   || "font/woff2"
        "file.ttf"     || "font/ttf"
        "file.tiff"    || "image/tiff"
        "file.css"     || "text/css"
        "file.jpeg"    || "image/jpeg"
        "file.jpg"     || "image/jpeg"
        "file.JPG"     || "image/jpeg"
        "file.xml"     || "application/xml"
        "file.json"    || "application/json"
        "file.zip"     || "application/zip"
        "file.tar"     || "application/x-tar"
        "file.rar"     || "application/x-rar-compressed"
        "file.tar.gz"  || "application/x-gzip"
        "file.tar.bz2" || "application/x-bzip2"
        "file.pdf"     || "application/pdf"
        "file.xls"     || "application/vnd.ms-excel"
        "file.xlsx"    || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "file.doc"     || "application/msword"
        "file.docx"    || "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "file.ico"     || "image/x-icon"
        "file.ppt"     || "application/vnd.ms-powerpoint"
    }

    def "GetByExt"() {
        expect:
        MimeUtil.getByExt(fileName) == result
        where:
        fileName || result
        ""       || "application/octet-stream"
        "file."  || "application/octet-stream"
        "js"     || "application/javascript"
        "png"    || "image/png"
        "Js"     || "application/javascript"
        "JS"     || "application/javascript"
        "bmp"    || "image/bmp"
        "woff"   || "font/woff"
        "woff2"  || "font/woff2"
        "ttf"    || "font/ttf"
        "tiff"   || "image/tiff"
        "css"    || "text/css"
        "jpeg"   || "image/jpeg"
        "jpg"    || "image/jpeg"
        "JPG"    || "image/jpeg"
        "xml"    || "application/xml"
        "json"   || "application/json"
        "zip"    || "application/zip"
        "tar"    || "application/x-tar"
        "rar"    || "application/x-rar-compressed"
        "gz"     || "application/x-gzip"
        "bz2"    || "application/x-bzip2"
        "pdf"    || "application/pdf"
        "xls"    || "application/vnd.ms-excel"
        "xlsx"   || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "doc"    || "application/msword"
        "docx"   || "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "ico"    || "image/x-icon"
        "ppt"    || "application/vnd.ms-powerpoint"
    }
}
