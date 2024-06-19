package com.eslibs.common.converter;

import java.util.*;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 09.02.2021
 */
public interface ConvertOption {

    class Set extends LinkedHashSet<ConvertOption> {

        public Set(Collection<? extends ConvertOption> c, ConvertOption... options) {
            super(c);
            addAll(new LinkedHashSet<>(Arrays.stream(options).filter(Objects::nonNull).toList()));
        }

        public Set(ConvertOption... options) {
            super();
            addAll(new HashSet<>(Arrays.stream(options).filter(Objects::nonNull).toList()));
        }
    }
}
