package com.es.lib.common.exception.web;

public class UpgradeRequiredException extends CodeRuntimeException {

    public UpgradeRequiredException(String message) {
        super(message);
    }

    public UpgradeRequiredException(String message, String code) {
        super(message, code);
    }

    public UpgradeRequiredException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public UpgradeRequiredException(Throwable cause, String code) {
        super(cause, code);
    }

    public UpgradeRequiredException(String message, String code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, code, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "UpgradeRequiredException{} " + super.toString();
    }
}
