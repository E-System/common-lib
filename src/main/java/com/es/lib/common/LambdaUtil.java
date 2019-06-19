package com.es.lib.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class LambdaUtil {

    private LambdaUtil() { }

    public static void execute(ThrowableRunnable executor) {
        execute(executor, null);
    }

    public static void execute(ThrowableRunnable executor, Consumer<Throwable> exceptionConsumer) {
        try {
            executor.run();
        } catch (Throwable t) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(t);
            }
        }
    }

    public interface ThrowableRunnable {

        void run() throws Throwable;
    }

    public static <T> T safeGet(Supplier<T> supplier) {
        return safeGet(supplier, (T) null);
    }

    public static <T> T safeGet(Supplier<T> supplier, Supplier<T> defaultSupplier) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return defaultSupplier != null ? defaultSupplier.get() : null;
        }
    }

    public static <T> T safeGet(Supplier<T> supplier, T defaultValue) {
        return safeGet(supplier, (Supplier<T>) () -> defaultValue);
    }
}
