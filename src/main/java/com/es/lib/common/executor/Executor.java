package com.es.lib.common.executor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@Builder(toBuilder = true)
@AllArgsConstructor
public class Executor {

    public static final int DEFAULT_MAX_POOL_SIZE = 200;
    public static final int DEFAULT_TERMINATE_TIMEOUT = 15;
    public static final int DEFAULT_TERMINATE_TIMEOUT_AFTER_SHUTDOWN = 5;

    @Builder.Default
    private final int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
    @Builder.Default
    private final int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    @Builder.Default
    private final int terminateTimeout = DEFAULT_TERMINATE_TIMEOUT;
    @Builder.Default
    private final int terminateTimeoutAfterShutdown = DEFAULT_TERMINATE_TIMEOUT_AFTER_SHUTDOWN;
    @Builder.Default
    private final Logger log = LoggerFactory.getLogger(Executor.class);

    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void run(Consumer<ExecutorService> executor) {
        get(v -> {
            executor.accept(v);
            return null;
        });
    }

    public <R> R get(Function<ExecutorService, R> executor) {
        ExecutorService executorService = executorService();
        try {
            return executor.apply(executorService);
        } finally {
            shutdownAndAwaitTermination(executorService);
        }
    }

    public void shutdownAndAwaitTermination(ExecutorService executorService) {
        shutdownAndAwaitTermination(executorService, terminateTimeout, terminateTimeoutAfterShutdown);
    }

    public void shutdownAndAwaitTermination(ExecutorService executorService, long firstTimeout, long secondTimeout) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(firstTimeout, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(secondTimeout, TimeUnit.SECONDS)) {
                    log.error("ExecutorService не завершился принудительно!");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
