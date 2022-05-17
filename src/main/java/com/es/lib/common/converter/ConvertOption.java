package com.es.lib.common.converter;

import java.util.*;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 09.02.2021
 */
public interface ConvertOption {

    class Set extends LinkedHashSet<ConvertOption> {

        public Set(Collection<? extends ConvertOption> c, ConvertOption... options) {
            super(c);
            addAll(new LinkedHashSet<>(Arrays.asList(options)));
        }

        public Set(ConvertOption... options) {
            super();
            addAll(new HashSet<>(Arrays.asList(options)));
        }
    }

    static <A> Optional<A> get(java.util.Set<ConvertOption> options, Class<A> cls) {
        return options.stream().filter(v -> v.getClass().isAssignableFrom(cls)).map(v -> (A) v).findFirst();
    }
}
