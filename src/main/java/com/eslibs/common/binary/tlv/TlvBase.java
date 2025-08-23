package com.eslibs.common.binary.tlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
abstract class TlvBase<T> implements ITlv {

    @Getter
    protected final int tag;
    @Getter
    protected final T value;
    protected final Predicate<T> validator;

    @Override
    public boolean isValid() {
        return validator == null || validator.test(value);
    }
}
