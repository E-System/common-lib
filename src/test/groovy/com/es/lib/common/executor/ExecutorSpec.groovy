package com.es.lib.common.executor

import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class ExecutorSpec extends Specification {

    def "Run and get with Executor"() {
        given:
        Executor executor = Executor.builder().build()
        AtomicInteger counter = new AtomicInteger()

        when:
        executor.run { es ->
            es.submit { counter.incrementAndGet() }.get()
        }

        then:
        counter.get() == 1

        when:
        def result = executor.get({ es ->
            return es.submit({ counter.addAndGet(10) } as java.util.concurrent.Callable<Integer>).get()
        } as java.util.function.Function)

        then:
        result == 11
        counter.get() == 11
    }

    def "Executor service creation and shutdown"() {
        given:
        Executor executor = Executor.builder()
                .corePoolSize(2)
                .maxPoolSize(4)
                .terminateTimeout(1)
                .terminateTimeoutAfterShutdown(1)
                .build()

        when:
        def service = executor.executorService()

        then:
        !service.isShutdown()

        when:
        executor.shutdownAndAwaitTermination(service)

        then:
        service.isShutdown()
    }
}
