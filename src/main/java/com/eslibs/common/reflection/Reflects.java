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

package com.eslibs.common.reflection;

import com.eslibs.common.collection.Items;
import com.eslibs.common.exception.ESRuntimeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.reflections.scanners.Scanners.*;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reflects {

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

    public static Map<String, Method> getDeclaredMethods(Class<?> cls) {
        return getDeclaredMethods(cls, null);
    }

    public static Map<String, Method> getDeclaredMethods(Class<?> cls, Predicate<Method> methodFilter) {
        Map<String, Method> result = new LinkedHashMap<>();
        Class<?> superclass = cls.getSuperclass();
        if (superclass != null) {
            result.putAll(getDeclaredMethods(superclass, methodFilter));
        }
        for (Method method : cls.getDeclaredMethods()) {
            if (methodFilter != null && !methodFilter.test(method)) {
                continue;
            }
            result.put(method.getName(), method);
        }
        return result;
    }

    public static Map<String, Field> getDeclaredFields(Class<?> cls) {
        return getDeclaredFields(cls, null);
    }

    public static Map<String, Field> getDeclaredFields(Class<?> cls, Class<? extends Annotation> annotationClass) {
        Map<String, Field> result = new LinkedHashMap<>();
        Class<?> superclass = cls.getSuperclass();
        if (superclass != null) {
            result.putAll(getDeclaredFields(superclass, annotationClass));
        }
        for (Field field : cls.getDeclaredFields()) {
            if (annotationClass != null && !field.isAnnotationPresent(annotationClass)) {
                continue;
            }
            result.put(field.getName(), field);
        }
        return result;
    }

    public static <T extends Annotation> Collection<String> getDeclaredFieldNames(Class<?> cls, Class<T> annotationClass, BiFunction<Field, T, String> converter) {
        Map<String, Field> declaredFields = getDeclaredFields(cls, annotationClass);
        if (converter == null) {
            return declaredFields.keySet();
        }
        Collection<String> result = new HashSet<>();
        for (Map.Entry<String, Field> entry : declaredFields.entrySet()) {
            result.add(converter.apply(entry.getValue(), entry.getValue().getAnnotation(annotationClass)));
        }
        return result;
    }

    public static <T> Map<String, String> getPropertyNameBySetter(Class<T> tClass) {
        Map<String, Method> methods = getDeclaredMethods(tClass, method -> method.getName().startsWith("set"));
        Map<String, Field> fields = getDeclaredFields(tClass, JsonProperty.class);
        Map<String, String> result = new HashMap<>();
        for (String methodName : methods.keySet()) {
            String fieldName = StringUtils.uncapitalize(methodName.replace("set", ""));
            Field field = fields.get(fieldName);
            if (field == null) {
                continue;
            }
            result.put(methodName, field.getAnnotation(JsonProperty.class).value());
        }
        return result;
    }

    public static Type[] extractTypes(Type type) {
        Type next = type;
        while (next instanceof Class<?> cls) {
            next = cls.getGenericSuperclass();
        }
        if (Objects.requireNonNull(next) instanceof ParameterizedType v) {
            return v.getActualTypeArguments();
        }
        throw new ESRuntimeException("Entity type not found with " + type);
    }

    public static Class<?> extractClass(Type type) {
        return switch (type) {
            case Class<?> v -> v;
            case ParameterizedType v -> (Class<?>) v.getOwnerType();
            default -> throw new ESRuntimeException("Unknown type: " + type);
        };
    }

    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {
        return cls.getAnnotation(annotationClass);
    }

    public static <T> Collection<T> getStaticObjects(Class<?> holder) throws IllegalAccessException {
        Collection<T> result = new LinkedList<>();
        if (holder != null) {
            for (Field field : holder.getFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && !field.getName().startsWith("$")) {
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
        return new Reflections(builder).get(SubTypes.of(TypesAnnotated.with(annotation)).asClass());
    }

    public static Set<String> getResources(String prefix, Predicate<String> namePredicate) {
        return getResources(Collections.singletonList(prefix), namePredicate);
    }

    public static Set<String> getResources(Collection<String> packages, Predicate<String> namePredicate) {
        if (namePredicate == null) {
            namePredicate = v -> true;
        }
        ConfigurationBuilder builder = new ConfigurationBuilder();
        packages.forEach(builder::forPackages);
        builder.addScanners(Resources);
        return new Reflections(builder).get(Resources.with(".*").filter(namePredicate));
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

    public static <T> T newInstance(Class<T> instanceClass) {
        try {
            return instanceClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new ESRuntimeException(e);
        }
    }

    public static FieldMeta fieldMeta(Class<?> aClass, String capitalizedField) throws RuntimeException {
        String key = aClass.getCanonicalName() + "." + capitalizedField;
        return GETTER_HASH_MAP.computeIfAbsent(key, v -> {
            Method getter = null;
            try {
                getter = aClass.getMethod("get" + capitalizedField);
            } catch (NoSuchMethodException e) {
                try {
                    getter = aClass.getMethod("is" + capitalizedField);
                } catch (NoSuchMethodException ignore) {}
            }
            if (getter == null) {
                throw new RuntimeException("Getter not found for field: " + capitalizedField);
            }
            Method setter;
            try {
                setter = aClass.getMethod("set" + capitalizedField, getter.getReturnType());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Setter not found for field: " + capitalizedField);
            }
            return new FieldMeta(getter, setter);
        });
    }

    public record FieldMeta(Method getter, Method setter) {}

    private static final Map<String, FieldMeta> GETTER_HASH_MAP = new ConcurrentHashMap<>();
}
