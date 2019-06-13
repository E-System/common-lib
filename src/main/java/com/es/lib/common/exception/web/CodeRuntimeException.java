package com.es.lib.common.exception.web;

public class CodeRuntimeException extends RuntimeException {

    private String code;

    public CodeRuntimeException(String message) {
        super(message);
    }

    public CodeRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public CodeRuntimeException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodeRuntimeException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public CodeRuntimeException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    @Override
    public String toString() {
        return "CodeRuntimeException{" +
               "code='" + code + '\'' +
               "} " + super.toString();
    }
}
