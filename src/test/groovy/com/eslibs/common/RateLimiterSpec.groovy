package com.eslibs.common

import com.eslibs.common.RateLimiter
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class RateLimiterSpec extends Specification {

    def "Acquire"() {
        when:
        def rateLimiter = new RateLimiter(1, TimeUnit.SECONDS)
        def time1 = System.currentTimeMillis()
        rateLimiter.acquire()
        def diff1 = System.currentTimeMillis() - time1
        def time2 = System.currentTimeMillis()
        rateLimiter.acquire()
        def diff2 = System.currentTimeMillis() - time1
        then:
        diff1 <= TimeUnit.SECONDS.toMillis(1)
        diff2 <= TimeUnit.SECONDS.toMillis(2) && diff2 >= TimeUnit.SECONDS.toMillis(1)
    }
}
