package com.eslibs.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class MethodNotAllowedException extends CodeRuntimeException {

    public MethodNotAllowedException(String code, String message) {
        super(code, message);
    }

    public MethodNotAllowedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
