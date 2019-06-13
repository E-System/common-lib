package com.es.lib.common.exception.web;

public class NotImplementedException extends CodeRuntimeException {

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, String code) {
        super(message, code);
    }

    public NotImplementedException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public NotImplementedException(Throwable cause, String code) {
        super(cause, code);
    }

    public NotImplementedException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "NotImplementedException{} " + super.toString();
    }
}
