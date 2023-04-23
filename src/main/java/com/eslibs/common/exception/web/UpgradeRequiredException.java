package com.eslibs.common.exception.web;

import lombok.ToString;

@ToString(callSuper = true)
public class UpgradeRequiredException extends CodeRuntimeException {

    public UpgradeRequiredException(String code, String message) {
        super(code, message);
    }

    public UpgradeRequiredException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
