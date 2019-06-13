package com.es.lib.common.exception.web;

public class UnauthorizedException extends CodeRuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String code) {
        super(message, code);
    }

    public UnauthorizedException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public UnauthorizedException(Throwable cause, String code) {
        super(cause, code);
    }

    public UnauthorizedException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "UnauthorizedException{} " + super.toString();
    }
}
