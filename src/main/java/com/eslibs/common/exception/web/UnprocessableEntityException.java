package com.eslibs.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class UnprocessableEntityException extends CodeRuntimeException {

    public UnprocessableEntityException(String code, String message) {
        super(code, message);
    }

    public UnprocessableEntityException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
