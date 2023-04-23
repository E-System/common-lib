package com.eslibs.common.validation;

public enum OgrnType {
    ANY(null),
    OGRN(13),
    OGRNIP(15);
    private Integer value;

    OgrnType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}