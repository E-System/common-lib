package com.eslibs.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Lambda {

    public static void run(ExceptionalRunnable runnable) {
        run(runnable, null);
    }

    public static void run(ExceptionalRunnable runnable, Consumer<Throwable> exceptionConsumer) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(t);
            }
        }
    }

    public static <T> T get(ExceptionalSupplier<T> supplier, Function<Throwable, T> defaultSupplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            if (defaultSupplier != null) {
                return defaultSupplier.apply(t);
            }
            throw new RuntimeException(t);
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
    public interface ExceptionalRunnable {

        void run() throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionalSupplier<R> {

        R get() throws Exception;
    }
}
