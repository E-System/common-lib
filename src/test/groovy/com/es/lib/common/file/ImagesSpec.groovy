package com.es.lib.common.file


import spock.lang.Specification

class ImagesSpec extends Specification {

    def "Info"() {
        when:
        def res = Images.info(Images.class.getResourceAsStream("/Squirrel.jpg"))
        then:
        res != null
        res.width == 1024
        res.height == 768
        !res.vertical
    }
}
