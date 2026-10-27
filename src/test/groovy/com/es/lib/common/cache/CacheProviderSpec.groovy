package com.es.lib.common.cache

import spock.lang.Specification

class CacheProviderSpec extends Specification {

    def "Put and get cache items"() {
        given:
        CacheProvider cache = new CacheProvider()

        when:
        cache.put("k1", "v1")
        cache.put("k2", "v2", 1000L)

        then:
        cache.get("k1") == "v1"
        cache.get("k2") == "v2"
        cache.get("k3") == null
    }

    def "ComputeIfAbsent caches supplier result"() {
        given:
        CacheProvider cache = new CacheProvider()
        int callCount = 0

        when:
        def val1 = cache.computeIfAbsent("k1") {
            callCount++
            return "calculated1"
        }
        def val2 = cache.computeIfAbsent("k1") {
            callCount++
            return "calculated1_again"
        }
        def val3 = cache.computeIfAbsent("k2", 5000L) {
            callCount++
            return "calculated2"
        }

        then:
        val1 == "calculated1"
        val2 == "calculated1"
        val3 == "calculated2"
        callCount == 2
    }

    def "Clean removes expired items"() {
        given:
        CacheProvider cache = new CacheProvider()
        cache.put("expired", "val_expired", -1000L)
        cache.put("valid", "val_valid", 100000L)

        when:
        cache.clean()

        then:
        cache.get("expired") == null
        cache.get("valid") == "val_valid"
    }

    def "Item create with default and custom TTL"() {
        when:
        def item1 = CacheProvider.Item.create("d1", null)
        def item2 = CacheProvider.Item.create("d2", 500L)

        then:
        item1.data == "d1"
        item1.ttl != null
        item2.data == "d2"
        item2.ttl != null
    }
}
