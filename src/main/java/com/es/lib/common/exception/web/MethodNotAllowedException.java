package com.es.lib.common.exception.web;

public class MethodNotAllowedException extends CodeRuntimeException {

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(String message, String code) {
        super(message, code);
    }

    public MethodNotAllowedException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public MethodNotAllowedException(Throwable cause, String code) {
        super(cause, code);
    }

    public MethodNotAllowedException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "MethodNotAllowedException{} " + super.toString();
    }
}
