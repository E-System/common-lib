package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class UnauthorizedException extends CodeRuntimeException {

    public UnauthorizedException(String code, String message) {
        super(code, message);
    }

    public UnauthorizedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
