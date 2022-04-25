package com.es.lib.common.reflection;

import javassist.util.proxy.MethodHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ProxyFactory {

    private static final Map<Class<?>, Data> FACTORIES = new HashMap<>();

    public static <T> T create(Class<T> tClass, Map<String, Object> values) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Data data = FACTORIES.computeIfAbsent(tClass, v -> {
            javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
            factory.setSuperclass(v);
            factory.setFilter(m -> m.getName().startsWith("set"));
            return new Data(factory, Reflects.getPropertyNameBySetter(v));
        });
        return data.create(values);
    }

    public static <T> Map<String, Object> create(Class<T> tClass, Consumer<T> consumer) {
        try {
            Map<String, Object> values = new HashMap<>();
            T proxy = create(tClass, values);
            consumer.accept(proxy);
            return values;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequiredArgsConstructor
    private static class CollectHandler implements MethodHandler {

        private final Map<String, Object> values;
        private final Map<String, String> fields;

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            values.put(fields.get(thisMethod.getName()), args[0]);
            return null;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class Data {

        private final javassist.util.proxy.ProxyFactory factory;
        private final Map<String, String> fields;

        public <T> T create(Map<String, Object> values) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            return (T) factory.create(new Class<?>[0], new Object[0], new CollectHandler(values, fields));
        }


    }
}