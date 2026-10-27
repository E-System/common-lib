package com.es.lib.common

import spock.lang.Specification

class ComparisonOperatorSpec extends Specification {

    def "Check values"() {
        expect:
        ComparisonOperator.EQ.value == "="
        ComparisonOperator.GT.value == ">"
        ComparisonOperator.GE.value == ">="
        ComparisonOperator.LT.value == "<"
        ComparisonOperator.LE.value == "<="
    }
}
