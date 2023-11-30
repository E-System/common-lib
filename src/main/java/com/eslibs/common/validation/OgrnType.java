package com.eslibs.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OgrnType {

    ANY(null),
    OGRN(13),
    OGRNIP(15);
    private final Integer value;
}