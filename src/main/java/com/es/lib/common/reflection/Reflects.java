/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.reflection;

import com.es.lib.common.collection.Items;
import com.es.lib.common.exception.ESRuntimeException;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class Reflects {

    private Reflects() { }

    public static Map<String, Object> toMap(final Object instance, Function<Object, Object> converter) {
        return toMap(instance, null, converter);
    }

    public static Map<String, Object> toMap(final Object instance, Collection<String> exclude, Function<Object, Object> converter) {
        return toMap(instance, exclude, true, converter);
    }

    public static Map<String, Object> toMap(final Object instance, Collection<String> exclude, boolean excludeStatic, Function<Object, Object> converter) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Field> fields = getDeclaredFields(instance.getClass());
        boolean checkExclude = Items.isNotEmpty(exclude);
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();
            if (excludeStatic && Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (checkExclude && exclude.contains(entry.getKey())) {
                continue;
            }
            boolean notAccessible = !field.isAccessible();
            if (notAccessible) {
                field.setAccessible(true);
            }
            Object value = null;
            try {
                value = field.get(instance);
            } catch (Exception ignore) {}
            if (notAccessible) {
                field.setAccessible(false);
            }
            result.put(entry.getKey(), converter != null ? converter.apply(value) : value);
        }
        return result;
    }

    public static Map<String, Field> getDeclaredFields(Class<?> cls) {
        Map<String, Field> result = new LinkedHashMap<>();
        Class<?> superclass = cls.getSuperclass();
        if (superclass != null) {
            result.putAll(getDeclaredFields(superclass));
        }
        for (Field field : cls.getDeclaredFields()) {
            result.put(field.getName(), field);
        }
        return result;
    }

    public static Type[] extractTypes(Type type) {
        Type[] result = null;
        if (type instanceof ParameterizedType) {
            result = ((ParameterizedType) type).getActualTypeArguments();
        } else if (type instanceof Class) {
            Class<?> injectionPointClass = (Class<?>) type;
            result = ((ParameterizedType) injectionPointClass.getGenericSuperclass()).getActualTypeArguments();
        }
        if (result == null) {
            throw new ESRuntimeException("Entity type not found with " + type);
        }
        return result;
    }

    public static Class<?> extractClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getOwnerType();
        }
        throw new ESRuntimeException("Unknown type: " + type);
    }

    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {
        return cls.getAnnotation(annotationClass);
    }

    public static <T> Collection<T> getStaticObjects(Class<?> holder) throws IllegalAccessException {
        Collection<T> result = new LinkedList<>();
        if (holder != null) {
            for (Field field : holder.getFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                    result.add((T) field.get(holder));
                }
            }
        }
        return result;
    }

    public static Class<?> getInnerClassByName(Class<?> holder, String name) {
        for (Class<?> aClass : holder.getClasses()) {
            if (aClass.getSimpleName().equals(name)) {
                return aClass;
            }
        }
        return null;
    }

    public static Map<String, Class<?>> getInnerClassesGroupedByName(Class<?> holder) {
        Map<String, Class<?>> result = new HashMap<>();
        for (Class<?> aClass : holder.getClasses()) {
            result.put(aClass.getSimpleName(), aClass);
        }
        return result;
    }

    public static Set<Class<?>> getTypesAnnotatedWith(String packageName, Class<? extends Annotation> annotation) {
        return getTypesAnnotatedWith(Collections.singletonList(packageName), annotation);
    }

    public static Set<Class<?>> getTypesAnnotatedWith(Collection<String> packages, Class<? extends Annotation> annotation) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        packages.forEach(builder::forPackages);
        return new Reflections(builder).getTypesAnnotatedWith(annotation);
    }

    public static Set<String> getResources(String prefix, Predicate<String> namePredicate) {
        if (namePredicate == null) {
            namePredicate = v -> true;
        }
        return new Reflections(prefix, new ResourcesScanner()).getResources(namePredicate);
    }

    public static <T> Collection<T> getInnerClassStaticObjectByName(Class<?> holder, String name) throws IllegalAccessException {
        return getStaticObjects(getInnerClassByName(holder, name));
    }

    public static Object createDefaultInstance(Class<?> valueClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        try {
            return create(valueClass.getDeclaredConstructor());
        } catch (NoSuchMethodException ignore) {}
        try {
            return create(valueClass.getDeclaredConstructor(String.class), "0");
        } catch (NoSuchMethodException ignore) {}
        try {
            return create(valueClass.getDeclaredConstructor(Integer.class), 0);
        } catch (NoSuchMethodException ignore) {}
        try {
            return create(valueClass.getDeclaredConstructor(Short.class), (short) 0);
        } catch (NoSuchMethodException ignore) {}
        try {
            return create(valueClass.getDeclaredConstructor(Long.class), 0L);
        } catch (NoSuchMethodException ignore) {}
        try {
            return create(valueClass.getDeclaredConstructor(Double.class), 0.0d);
        } catch (NoSuchMethodException ignore) {}
        return valueClass;
    }

    private static Object create(Constructor<?> constructor, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean constructorNotAccessible = !constructor.isAccessible();
        if (constructorNotAccessible) {
            constructor.setAccessible(true);
        }
        Object result = constructor.newInstance(args);
        if (constructorNotAccessible) {
            constructor.setAccessible(false);
        }
        return result;
    }
}
