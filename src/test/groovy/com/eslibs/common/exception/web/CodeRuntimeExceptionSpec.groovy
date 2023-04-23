package com.eslibs.common.exception.web

import spock.lang.Specification

class CodeRuntimeExceptionSpec extends Specification {

    def "Code not empty"() {
        expect:
        new CodeRuntimeException("Code", "Message").code == 'Code'
        new CodeRuntimeException("Code", "Message", new Throwable()).code == 'Code'
    }
}
