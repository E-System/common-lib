package com.es.lib.common.converter.option

import spock.lang.Specification

class LinkedConvertOptionSpec extends Specification {

    def "LinkedConvertOption get items"() {
        given:
        LinkedConvertOption<String, Integer> opt1 = new LinkedConvertOption<>()
        LinkedConvertOption<String, Integer> opt2 = new LinkedConvertOption<>("a", 1)
        LinkedConvertOption<String, Integer> opt3 = new LinkedConvertOption<>(["x": 10, "y": 20])

        expect:
        opt1.get("a") == null
        opt2.get("a") == 1
        opt3.get("x") == 10
        opt3.get("y") == 20
    }

    def "LinkedCollectionConvertOption get and getFirst"() {
        given:
        LinkedCollectionConvertOption<String, Integer> optSingle = new LinkedCollectionConvertOption<>("k1", 100)
        LinkedCollectionConvertOption<String, Integer> optMulti = new LinkedCollectionConvertOption<>("k2", [200, 300])
        LinkedCollectionConvertOption<String, Integer> optMap = new LinkedCollectionConvertOption<>(["k3": [400, 500]])
        LinkedCollectionConvertOption<String, Integer> optEmpty = new LinkedCollectionConvertOption<>()

        expect:
        optSingle.get("k1") == [100]
        optSingle.getFirst("k1") == 100
        optMulti.get("k2") == [200, 300]
        optMulti.getFirst("k2") == 200
        optMap.getFirst("k3") == 400
        optEmpty.getFirst("unknown") == null
    }
}
