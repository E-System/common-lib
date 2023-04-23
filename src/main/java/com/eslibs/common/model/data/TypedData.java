package com.eslibs.common.model.data;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public abstract class TypedData extends OutputData {

    private final String contentType;

    public TypedData(String fileName, String contentType) {
        super(fileName);
        this.contentType = contentType;
    }
}
