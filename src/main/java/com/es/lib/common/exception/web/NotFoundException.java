package com.es.lib.common.exception.web;

public class NotFoundException extends CodeRuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String code) {
        super(message, code);
    }

    public NotFoundException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public NotFoundException(Throwable cause, String code) {
        super(cause, code);
    }

    public NotFoundException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "NotFoundException{} " + super.toString();
    }
}
