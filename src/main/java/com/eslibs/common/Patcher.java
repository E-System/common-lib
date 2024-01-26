package com.eslibs.common;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class Patcher<T, R> {

    @Getter
    private final T from;
    @Getter
    private final R to;
    private final Set<String> fields;
    private final Collection<Rule<T, R, ?, ?>> rules = new ArrayList<>();

    public static <T, R> Patcher<T, R> create(T from, R to) {
        return create(from, to, null);
    }

    public static <T, R> Patcher<T, R> create(T from, R to, Collection<String> fields) {
        return create(from, to, fields != null ? new HashSet<>(fields) : null);
    }

    public static <T, R> Patcher<T, R> create(T from, R to, Set<String> fields) {
        return new Patcher<>(from, to, fields);
    }

    public Patcher<T, R> rule(String field) {
        return rule(field, false);
    }

    public Patcher<T, R> rule(String field, boolean checkChange) {
        return rule(field, checkChange, null);
    }

    public Patcher<T, R> rule(String field, boolean checkChange, Consumer<Updated> updatedFieldConsumer) {
        if (checkChange) {
            return rule(new Rule<>(this, field, null, updatedFieldConsumer));
        } else {
            return rule(field, () -> reflective(from, to, field, null));
        }
    }

    public Patcher<T, R> rule(String field, Runnable runnable) {
        return rule(new Rule<>(this, field, runnable));
    }

    public Patcher<T, R> rule(String field, BiConsumer<T, R> consumer) {
        return rule(new Rule<>(this, field, consumer));
    }

    public <R1> Patcher<T, R> rule(String field, Function<T, R1> fromGetter, BiConsumer<R, R1> toSetter) {
        return rule(field, fromGetter, toSetter, v -> v);
    }

    public <R1, R2> Patcher<T, R> rule(String field, Function<T, R1> fromGetter, BiConsumer<R, R2> toSetter, Function<R1, R2> converter) {
        return rule(field, fromGetter, toSetter, converter, null, null);
    }

    public <R1, R2> Patcher<T, R> rule(String field, Function<T, R1> fromGetter, BiConsumer<R, R2> toSetter, Function<R1, R2> converter, Function<R, R2> callbackGetter, Function<R2, String> callbackConverter) {
        return rule(field, fromGetter, toSetter, converter, callbackGetter, callbackConverter, null);
    }

    public <R1, R2> Patcher<T, R> rule(String field, Function<T, R1> fromGetter, BiConsumer<R, R2> toSetter, Function<R1, R2> converter, Function<R, R2> callbackGetter, Function<R2, String> callbackConverter, Consumer<Updated> updatedFieldConsumer) {
        return rule(new Rule<>(this, field, fromGetter, toSetter, converter, callbackGetter, callbackConverter, updatedFieldConsumer));
    }

    private <R1, R2> Patcher<T, R> rule(Rule<T, R, R1, R2> rule) {
        rules.add(rule);
        return this;
    }

    public Result apply() {
        return apply(null);
    }

    public Result apply(Map<String, String> labels) {
        Collection<Updated> result = new ArrayList<>();
        for (Rule<T, R, ?, ?> rule : rules) {
            rule.invoke(fields, result, labels);
        }
        return new Result(result);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Result(Collection<Updated> items) {}

    @ToString
    @Getter
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            defaultImpl = UpdatedField.class,
            visible = true,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = UpdatedField.class, name = "F"),
            @JsonSubTypes.Type(value = UpdatedRow.class, name = "R"),
            @JsonSubTypes.Type(value = UpdatedGroup.class, name = "G"),
    })
    public static class Updated {

        private final Type type;
        private final String field;

        public Updated(Type type, String field) {
            this.type = type != null ? type : Type.FIELD;
            this.field = field;
        }

        @Getter
        @RequiredArgsConstructor
        public enum Type {
            GROUP("G"),
            ROW("R"),
            FIELD("F");
            @JsonValue
            private final String value;
        }
    }

    @ToString
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdatedField extends Updated {

        private final String was;
        private final String became;

        public UpdatedField(String field, String was, String became) {
            this(Type.FIELD, field, was, became);
        }

        @JsonCreator
        public UpdatedField(@JsonProperty("type") Type type, @JsonProperty("field") String field, @JsonProperty("was") String was, @JsonProperty("became") String became) {
            super(type, field);
            this.was = was;
            this.became = became;
        }
    }

    @ToString
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdatedGroup extends Updated {

        private final Collection<Updated> items;

        public UpdatedGroup(String field, Collection<Updated> items) {
            this(Type.GROUP, field, items);
        }

        @JsonCreator
        public UpdatedGroup(@JsonProperty("type") Type type, @JsonProperty("field") String field, @JsonProperty("rows") Collection<Updated> items) {
            super(type, field);
            this.items = items;
        }
    }

    @ToString
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdatedRow extends UpdatedGroup {

        private final Event event;

        public UpdatedRow(String field, Event event) {
            this(field, event, null);
        }

        public UpdatedRow(String field, Event event, Collection<Updated> items) {
            this(Type.ROW, field, event, items);
        }

        @JsonCreator
        public UpdatedRow(@JsonProperty("type") Type type, @JsonProperty("field") String field, @JsonProperty("event") Event event, @JsonProperty("items") Collection<Updated> items) {
            super(type, field, items);
            this.event = event;
        }


        @Getter
        @RequiredArgsConstructor
        public enum Event {
            INSERT("I"),
            UPDATE("U"),
            DELETE("D");
            @JsonValue
            private final String value;
        }
    }

    @RequiredArgsConstructor
    private static class Rule<T, R, R1, R2> {

        private final Patcher<T, R> owner;
        private final String field;
        private final Runnable runnable;

        private final BiConsumer<T, R> consumer;

        private final Function<T, R1> fromGetter;
        private final BiConsumer<R, R2> toSetter;
        private final Function<R1, R2> converter;

        private final Function<R, R2> callbackGetter;
        private final Function<R2, String> callbackConverter;
        private final Consumer<Updated> updatedFieldCallback;

        public Rule(Patcher<T, R> owner, String field, Runnable runnable) {
            this(owner, field, runnable, null, null, null, null, null, null, null);
        }

        public Rule(Patcher<T, R> owner, String field, Function<R2, String> callbackConverter) {
            this(owner, field, callbackConverter, null);
        }

        public Rule(Patcher<T, R> owner, String field, Function<R2, String> callbackConverter, Consumer<Updated> updatedFieldConsumer) {
            this(owner, field, null, null, null, null, null, null, callbackConverter, updatedFieldConsumer);
        }

        public Rule(Patcher<T, R> owner, String field, BiConsumer<T, R> consumer) {
            this(owner, field, null, consumer, null, null, null, null, null, null);
        }

        public Rule(Patcher<T, R> owner, String field, Function<T, R1> fromGetter, BiConsumer<R, R2> toSetter, Function<R1, R2> converter, Function<R, R2> callbackGetter, Function<R2, String> callbackConverter, Consumer<Updated> updatedFieldConsumer) {
            this(owner, field, null, null, fromGetter, toSetter, converter, callbackGetter, callbackConverter, updatedFieldConsumer);
        }

        void invoke(Set<String> fields, Collection<Updated> updatedFields, Map<String, String> labels) {
            if (fields == null || fields.contains(field)) {
                if (runnable != null) {
                    runnable.run();
                } else if (consumer != null) {
                    consumer.accept(owner.from, owner.to);
                } else if (fromGetter != null) {
                    R2 newValue = converter.apply(fromGetter.apply(owner.from));
                    if (callbackGetter != null) {
                        R2 oldValue = callbackGetter.apply(owner.to);
                        String newValueString = newValue == null ? null : callbackConverter.apply(newValue);
                        String oldValueString = oldValue == null ? null : callbackConverter.apply(oldValue);
                        if (!Objects.equals(newValueString, oldValueString)) {
                            UpdatedField updatedField = new UpdatedField(getFieldName(field, labels), oldValueString, newValueString);
                            updatedFields.add(updatedField);
                            if (updatedFieldCallback != null) {
                                updatedFieldCallback.accept(updatedField);
                            }
                        }
                    }
                    toSetter.accept(owner.to, newValue);
                } else {
                    reflective(owner.from, owner.to, field, (newValue, capitalizedField) -> {
                        try {
                            Method toGetter = findGetter(owner.to, capitalizedField);
                            Object oldValue = toGetter.invoke(owner.to);
                            String newValueString = newValue == null ? null : newValue.toString();
                            String oldValueString = oldValue == null ? null : oldValue.toString();
                            if (!Objects.equals(newValueString, oldValueString)) {
                                UpdatedField updatedField = new UpdatedField(getFieldName(field, labels), oldValueString, newValueString);
                                updatedFields.add(updatedField);
                                if (updatedFieldCallback != null) {
                                    updatedFieldCallback.accept(updatedField);
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }

        private String getFieldName(String field, Map<String, String> labels) {
            if (labels == null) {
                return field;
            }
            return labels.getOrDefault(field, field);
        }
    }

    private static void reflective(Object from, Object to, String field, BiConsumer<Object, String> processor) {
        try {
            String capitalizedField = capitalize(field);
            Method fromGetter = findGetter(from, capitalizedField);
            Object newValue = fromGetter.invoke(from);
            if (processor != null) {
                processor.accept(newValue, capitalizedField);
            }
            Method toSetter = to.getClass().getMethod("set" + capitalizedField, fromGetter.getReturnType());
            toSetter.invoke(to, newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Method findGetter(Object from, String capitalizedField) throws NoSuchMethodException {
        Class<?> aClass = from.getClass();
        try {
            return aClass.getMethod("get" + capitalizedField);
        } catch (NoSuchMethodException e) {
            return aClass.getMethod("is" + capitalizedField);
        }
    }

    private static String capitalize(String value) {
        if (value == null) return null;
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}