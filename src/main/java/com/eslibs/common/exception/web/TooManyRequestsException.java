package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class TooManyRequestsException extends CodeRuntimeException {

    public TooManyRequestsException(String code, String message) {
        super(code, message);
    }

    public TooManyRequestsException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
