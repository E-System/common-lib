package com.eslibs.common

import com.eslibs.common.IExtIdsOwner
import spock.lang.Specification

class IExtIdsOwnerSpec extends Specification {
    static class TestClass implements IExtIdsOwner {

        Map<String, String> extIds
    }

    enum TestEnum {
        K1,
        K2
    }

    def "Set ext id"() {
        when:
        def item = new TestClass()
        then:
        item.extIds == null
        when:
        item.setExtId('KEY', 'VALUE')
        item.setExtId(TestEnum.K1, 'VALUE_K1')
        item.setExtId(TestEnum.K2, 'VALUE_K2')
        then:
        item.extIds != null
        item.extIds['KEY'] == 'VALUE'
        item.extIds[TestEnum.K1.name()] == 'VALUE_K1'
        item.extIds[TestEnum.K2.name()] == 'VALUE_K2'
    }
}
