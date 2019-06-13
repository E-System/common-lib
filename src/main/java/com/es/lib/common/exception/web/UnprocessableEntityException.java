package com.es.lib.common.exception.web;

public class UnprocessableEntityException extends CodeRuntimeException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, String code) {
        super(message, code);
    }

    public UnprocessableEntityException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public UnprocessableEntityException(Throwable cause, String code) {
        super(cause, code);
    }

    public UnprocessableEntityException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "UnprocessableEntityException{} " + super.toString();
    }
}
