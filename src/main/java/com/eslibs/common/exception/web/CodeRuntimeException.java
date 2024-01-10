package com.eslibs.common.exception.web;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CodeRuntimeException extends RuntimeException {

    private final String code;

    public CodeRuntimeException(String code, String message) {
        this(code, message, null);
    }

    public CodeRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
