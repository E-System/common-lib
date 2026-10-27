package com.es.lib.common.exception

import com.es.lib.common.exception.web.*
import spock.lang.Specification

class ExceptionSpec extends Specification {

    def "ExceptionUtil getRootCause"() {
        given:
        Throwable root = new IllegalArgumentException("Root error")
        Throwable intermediate = new RuntimeException("Intermediate", root)
        Throwable top = new Exception("Top", intermediate)

        expect:
        ExceptionUtil.getRootCause(top) == root
        ExceptionUtil.getRootCause(root) == root
        ExceptionUtil.getRootCause(new Exception("Single")) instanceof Exception
    }

    def "ESException and ESRuntimeException constructors"() {
        given:
        Throwable cause = new IOException("io")

        when:
        ESException e1 = new ESException()
        ESException e2 = new ESException("msg")
        ESException e3 = new ESException("msg", cause)
        ESException e4 = new ESException(cause)

        ESRuntimeException re1 = new ESRuntimeException()
        ESRuntimeException re2 = new ESRuntimeException("msg")
        ESRuntimeException re3 = new ESRuntimeException("msg", cause)
        ESRuntimeException re4 = new ESRuntimeException(cause)

        then:
        e1.message == null
        e2.message == "msg"
        e3.cause == cause
        e4.cause == cause

        re1.message == null
        re2.message == "msg"
        re3.cause == cause
        re4.cause == cause
    }

    def "CodeRuntimeException and Web Exceptions constructors and getters"() {
        given:
        Throwable cause = new RuntimeException("inner")

        when:
        CodeRuntimeException cre1 = new CodeRuntimeException("msg")
        CodeRuntimeException cre2 = new CodeRuntimeException("msg", "ERR_01")
        CodeRuntimeException cre3 = new CodeRuntimeException("msg", "ERR_02", cause)
        CodeRuntimeException cre4 = new CodeRuntimeException(cause, "ERR_03")
        CodeRuntimeException cre5 = new CodeRuntimeException("msg", "ERR_04", cause, true, true)
        cre1.setCode("ERR_UPDATED")

        then:
        cre1.code == "ERR_UPDATED"
        cre2.code == "ERR_01"
        cre3.code == "ERR_02"
        cre4.code == "ERR_03"
        cre5.code == "ERR_04"
        cre1.toString().contains("ERR_UPDATED")

        when:
        def badReq = [new BadRequestException("m"), new BadRequestException("m", "c"), new BadRequestException("m", "c", cause), new BadRequestException(cause, "c"), new BadRequestException("m", "c", cause, true, true)]
        def forbidden = [new ForbiddenException("m"), new ForbiddenException("m", "c"), new ForbiddenException("m", "c", cause), new ForbiddenException(cause, "c"), new ForbiddenException("m", "c", cause, true, true)]
        def notFound = [new NotFoundException("m"), new NotFoundException("m", "c"), new NotFoundException("m", "c", cause), new NotFoundException(cause, "c"), new NotFoundException("m", "c", cause, true, true)]
        def notAllowed = [new MethodNotAllowedException("m"), new MethodNotAllowedException("m", "c"), new MethodNotAllowedException("m", "c", cause), new MethodNotAllowedException(cause, "c"), new MethodNotAllowedException("m", "c", cause, true, true)]
        def notImpl = [new NotImplementedException("m"), new NotImplementedException("m", "c"), new NotImplementedException("m", "c", cause), new NotImplementedException(cause, "c"), new NotImplementedException("m", "c", cause, true, true)]
        def unauth = [new UnauthorizedException("m"), new UnauthorizedException("m", "c"), new UnauthorizedException("m", "c", cause), new UnauthorizedException(cause, "c"), new UnauthorizedException("m", "c", cause, true, true)]
        def unproc = [new UnprocessableEntityException("m"), new UnprocessableEntityException("m", "c"), new UnprocessableEntityException("m", "c", cause), new UnprocessableEntityException(cause, "c"), new UnprocessableEntityException("m", "c", cause, true, true)]
        def upgrade = [new UpgradeRequiredException("m"), new UpgradeRequiredException("m", "c"), new UpgradeRequiredException("m", "c", cause), new UpgradeRequiredException(cause, "c"), new UpgradeRequiredException("m", "c", cause, true, true)]

        then:
        badReq.every { it.toString().contains("BadRequestException") }
        forbidden.every { it.toString().contains("ForbiddenException") }
        notFound.every { it.toString().contains("NotFoundException") }
        notAllowed.every { it.toString().contains("MethodNotAllowedException") }
        notImpl.every { it.toString().contains("NotImplementedException") }
        unauth.every { it.toString().contains("UnauthorizedException") }
        unproc.every { it.toString().contains("UnprocessableEntityException") }
        upgrade.every { it.toString().contains("UpgradeRequiredException") }
    }
}
