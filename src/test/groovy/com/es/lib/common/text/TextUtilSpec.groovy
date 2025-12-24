package com.es.lib.common.text

import spock.lang.Specification

class TextUtilSpec extends Specification {

    def "Contains"() {
        expect:
        !TextUtil.contains(null, null)
        !TextUtil.contains("", null)
        !TextUtil.contains(null, "")
        TextUtil.contains("", "")
        TextUtil.contains("asd", "as")
        TextUtil.contains("Asd", "as")
        !TextUtil.contains("Asd", "as", false)
        TextUtil.contains("Asd", "aS")
        !TextUtil.contains("Asd", "aS", false)
        !TextUtil.contains("asd", "asb")
    }
}
