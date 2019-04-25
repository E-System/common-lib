package com.es.lib.common;

import java.util.function.Consumer;

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
}
