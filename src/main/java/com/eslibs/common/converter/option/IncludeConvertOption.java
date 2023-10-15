package com.eslibs.common.converter.option;

import com.eslibs.common.converter.ConvertOption;
import com.eslibs.common.text.Texts;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

@NoArgsConstructor
public class IncludeConvertOption extends LinkedHashSet<String> implements ConvertOption {


    public IncludeConvertOption(String items) {
        this(StringUtils.isBlank(items) ? Collections.emptySet() : new LinkedHashSet<>(Texts.splitBy(",").toList(items)));
    }

    public IncludeConvertOption(Collection<? extends String> c) {
        super(c);
    }

    public <R, T> R process(T in, R out, IncludeConsumer<T, R> consumer) {
        for (String item : this) {
            consumer.accept(item, in, out);
        }
        return out;
    }

    @FunctionalInterface
    public interface IncludeConsumer<T, R> {

        void accept(String field, T in, R out);
    }

}
