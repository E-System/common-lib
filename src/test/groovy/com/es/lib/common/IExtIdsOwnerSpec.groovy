package com.es.lib.common

import spock.lang.Specification

class IExtIdsOwnerSpec extends Specification {
    static class TestClass implements IExtIdsOwner {

        Map<String, String> extIds
    }

    def "Set ext id"() {
        when:
        def item = new TestClass()
        then:
        item.extIds == null
        when:
        item.setExtId('KEY', 'VALUE')
        then:
        item.extIds != null
        item.extIds['KEY'] == 'VALUE'
    }
}
