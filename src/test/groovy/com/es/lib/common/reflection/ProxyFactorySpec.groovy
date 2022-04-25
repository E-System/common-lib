package com.es.lib.common.reflection

import com.fasterxml.jackson.annotation.JsonProperty
import spock.lang.Specification

class ProxyFactorySpec extends Specification {

    def "Create proxy"() {
        when:
        def values = ProxyFactory.create(DTOAccount){
            it.setAccountName("Hello")
            it.setBillingContact2(new DTOReference("1"))
            it.setOwner(null)
        }
        System.out.println(values)
        then:
        values['Account_Name'] == 'Hello'
        (values['Billing_Contact2'] as DTOReference).id == '1'
        values['Owner'] == null
    }

    static class DTOReference {
        String id

        DTOReference(String id) {
            this.id = id
        }
    }

    static class DTOAccount {
        @JsonProperty("Account_Name")
        String accountName
        @JsonProperty("Billing_Contact2")
        DTOReference billingContact2
        @JsonProperty("Owner")
        String owner
    }
}
