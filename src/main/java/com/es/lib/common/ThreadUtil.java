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

package com.es.lib.common;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 12.06.15
 */
public class ThreadUtil {

    public static final int DEFAULT_MAX_POOL_SIZE = 200;
    public static final int DEFAULT_TERMINATE_TIMEOUT_1 = 15;
    public static final int DEFAULT_TERMINATE_TIMEOUT_2 = 5;

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static ExecutorService bgService() {
        return bgService(DEFAULT_MAX_POOL_SIZE);
    }

    public static ExecutorService bgService(int maxPoolSize) {
        return bgService(maxPoolSize, Runtime.getRuntime().availableProcessors() * 2);
    }

    public static ExecutorService bgService(int maxPoolSize, int corePoolSize) {
        return new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static void shutdownAndAwaitTermination(Logger log, ExecutorService executorService) {
        shutdownAndAwaitTermination(log, executorService, DEFAULT_TERMINATE_TIMEOUT_1, DEFAULT_TERMINATE_TIMEOUT_2);
    }

    public static void shutdownAndAwaitTermination(Logger log, ExecutorService executorService, long firstTimeout, long secondTimeout) {
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
