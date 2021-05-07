package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class BadRequestException extends CodeRuntimeException {

    public BadRequestException(String code, String message) {
        super(code, message);
    }

    public BadRequestException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
