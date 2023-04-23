package com.eslibs.common.store

import spock.lang.Specification

class IStoreSpec extends Specification {

    class Store implements IStore {
        String fileName
        String fileExt
        long crc32
        long size
        String mime
        String url

        Store(String fileName, String fileExt, String mime) {
            this.fileName = fileName
            this.fileExt = fileExt
            this.mime = mime
        }

        Store(String url) {
            this.url = url
        }

        @Override
        String getFullName() {
            return IStore.fullName(this)
        }

        @Override
        String getAbbreviatedFileName(int maxWidth) {
            return IStore.abbreviatedFileName(this, maxWidth)
        }

        @Override
        boolean isImage() {
            return IStore.isImage(this)
        }
    }

    def "IsOnlyLink"() {
        expect:
        !new Store('name', 'ext', 'image/png').onlyLink
        new Store('url').onlyLink
    }

    def "FullName"() {
        expect:
        new Store('name', 'ext', 'image/png').fullName == 'name.ext'
    }

    def "AbbreviatedFileName"() {
        expect:
        new Store('name', 'ext', 'image/png').getAbbreviatedFileName(10) == 'name.ext'
        new Store('name-name-name-name', 'ext', 'image/png').getAbbreviatedFileName(10) == 'na..me.ext'
    }

    def "IsImage"() {
        expect:
        new Store('name', 'ext', 'image/png').image
        !new Store('name', 'ext', 'text/plain').image
    }

    def "IsMime"() {
        expect:
        IStore.isMime(mime, part) == result
        where:
        mime                | part    || result
        null                | null     | false
        ""                  | ""       | true
        "application/image" | "image"  | true
        "application/image" | "image2" | false
    }

    def "IsImage static"() {
        expect:
        IStore.isImage("image/png")
        !IStore.isImage("octet/stream")
    }
}
