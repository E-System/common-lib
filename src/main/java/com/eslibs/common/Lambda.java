package com.eslibs.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Lambda {

    public static void run(Runnable executor) {
        run(executor, null);
    }

    public static void run(Runnable executor, Consumer<Throwable> exceptionConsumer) {
        try {
            executor.run();
        } catch (Throwable t) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(t);
            }
        }
    }

    public static <T> T run(ExceptionalSupplier<T> executor, Function<Throwable, T> exceptionConsumer) {
        try {
            return executor.run();
        } catch (Throwable t) {
            return exceptionConsumer.apply(t);
        }
    }

    public static <T> T get(Supplier<T> supplier) {
        return get(supplier, (T) null);
    }

    public static <T> T get(Supplier<T> supplier, Supplier<T> defaultSupplier) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return defaultSupplier != null ? defaultSupplier.get() : null;
        }
    }

    public static <T> T get(Supplier<T> supplier, T defaultValue) {
        return get(supplier, (Supplier<T>) () -> defaultValue);
    }

    @FunctionalInterface
    public interface Runnable {

        void run() throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionalSupplier<R> {

        R run() throws Exception;
    }
}
