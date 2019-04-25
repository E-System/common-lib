package com.es.lib.common

import spock.lang.Specification

import java.util.function.Consumer

class LambdaUtilSpec extends Specification {

    def "Execute with Exception ignore Exception"() {
        expect:
        LambdaUtil.execute(new LambdaUtil.ThrowableRunnable() {
            @Override
            void run() throws Throwable {
                throw new Exception()
            }
        })
    }

    def "Execute without Exception"() {
        when:
        def value = 1
        then:
        LambdaUtil.execute(new LambdaUtil.ThrowableRunnable() {
            @Override
            void run() throws Throwable {
                value += 1
            }
        })
        expect:
        value == 2
    }

    def "Execute with Exception consumer not throw"() {
        when:
        def value = 1
        def value2 = 1
        then:
        LambdaUtil.execute(new LambdaUtil.ThrowableRunnable() {
            @Override
            void run() throws Throwable {
                value += 1
            }
        }, new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) {
                value2 += 1
            }
        })
        expect:
        value == 2
        value2 == 1
    }

    def "Execute with Exception consumer with throw"() {
        when:
        def value = 1
        def value2 = 1
        then:
        LambdaUtil.execute(new LambdaUtil.ThrowableRunnable() {
            @Override
            void run() throws Throwable {
                value += 1
                throw new Exception()
            }
        }, new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) {
                value2 += 1
            }
        })
        expect:
        value == 2
        value2 == 2
    }

    def "Execute with Exception consumer with only throw"() {
        when:
        def value = 1
        def value2 = 1
        then:
        LambdaUtil.execute(new LambdaUtil.ThrowableRunnable() {
            @Override
            void run() throws Throwable {
                throw new Exception()
            }
        }, new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) {
                value2 += 1
            }
        })
        expect:
        value == 1
        value2 == 2
    }
}
