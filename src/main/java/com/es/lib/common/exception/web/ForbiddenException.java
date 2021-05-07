package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class ForbiddenException extends CodeRuntimeException {

    public ForbiddenException(String code, String message) {
        super(code, message);
    }

    public ForbiddenException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
