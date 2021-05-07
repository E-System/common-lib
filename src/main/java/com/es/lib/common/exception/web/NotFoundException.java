package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class NotFoundException extends CodeRuntimeException {

    public NotFoundException(String code, String message) {
        super(code, message);
    }

    public NotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
