package com.es.lib.common.exception.web;

public class BadRequestException extends CodeRuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, String code) {
        super(message, code);
    }

    public BadRequestException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public BadRequestException(Throwable cause, String code) {
        super(cause, code);
    }

    public BadRequestException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "BadRequestException{} " + super.toString();
    }
}
