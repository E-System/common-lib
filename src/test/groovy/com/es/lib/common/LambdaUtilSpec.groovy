package com.es.lib.common

import spock.lang.Specification

import java.util.function.Consumer
import java.util.function.Supplier

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

    class A {
        String a
    }

    class B {
        A a
    }

    class C {
        B b
    }

    def "Safe with null"() {
        expect:
        LambdaUtil.safeGet(new Supplier<String>() {
            @Override
            String get() {
                new C().b.a.a
            }
        }) == null
    }

    def "Safe with defaultValue"() {
        expect:
        LambdaUtil.safeGet(new Supplier<String>() {
            @Override
            String get() {
                new C().b.a.a
            }
        }, "512") == "512"
    }

    def "Safe with defaultSupplier"() {
        expect:
        LambdaUtil.safeGet(new Supplier<String>() {
            @Override
            String get() {
                new C().b.a.a
            }
        }, new Supplier<String>() {
            @Override
            String get() {
                return "444"
            }
        }) == "444"
    }

    def "Safe with filled"() {
        when:
        A a = new A()
        a.a = "123"
        B b = new B()
        b.a = a
        C c = new C()
        c.b = b
        then:
        LambdaUtil.safeGet(new Supplier<String>() {
            @Override
            String get() {
                c.b.a.a
            }
        }) == "123"
    }
}
