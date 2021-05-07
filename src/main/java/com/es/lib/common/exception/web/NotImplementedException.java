package com.es.lib.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class NotImplementedException extends CodeRuntimeException {

    public NotImplementedException(String code, String message) {
        super(code, message);
    }

    public NotImplementedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
