package com.es.lib.common.exception.web;

public class ForbiddenException extends CodeRuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, String code) {
        super(message, code);
    }

    public ForbiddenException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public ForbiddenException(Throwable cause, String code) {
        super(cause, code);
    }

    public ForbiddenException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "ForbiddenException{} " + super.toString();
    }
}
