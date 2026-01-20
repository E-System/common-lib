package com.es.lib.common.text

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import java.util.function.Function

class TextUtilSpec extends Specification {

    static class SplitItem {

        int v1
        int v2

        SplitItem(int v1, int v2) {
            this.v1 = v1
            this.v2 = v2
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            SplitItem splitItem = (SplitItem) o

            if (v1 != splitItem.v1) return false
            if (v2 != splitItem.v2) return false

            return true
        }

        int hashCode() {
            int result
            result = v1
            result = 31 * result + v2
            return result
        }
    }

    def "Split"() {
        expect:
        splitter.toList(value) == result
        where:
        value   | splitter                     || result
        ""      | TextUtil.splitBy(";")        || []
        "1;2"   | TextUtil.splitBy(";")        || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";")        || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";", true)  || ["1", "2"]
        "1; 2"  | TextUtil.splitBy(";", false) || ["1", " 2"]
        "1;; 2" | TextUtil.splitBy(";", 3)     || ["1", "", "2"]

    }

    def "Split to object"() {
        expect:
        splitter.toObject(value, new Function<String[], SplitItem>() {

            @Override
            SplitItem apply(String[] v) {
                return new SplitItem(Integer.parseInt(v[0]), Integer.parseInt(v[1]))
            }
        }) == result
        where:
        value   | splitter                     || result
        ""      | TextUtil.splitBy(";")        || null
        "1;2"   | TextUtil.splitBy(";")        || new SplitItem(1, 2)
        "1; 2"  | TextUtil.splitBy(";")        || new SplitItem(1, 2)
        "1; 2"  | TextUtil.splitBy(";", true)  || new SplitItem(1, 2)
        "1; 2"  | TextUtil.splitBy(";", false) || null
        "1;; 2" | TextUtil.splitBy(";", 3)     || null
    }

    def "Split1"() {
        expect:
        splitter.toPairs(value) == result
        where:
        value        | splitter                           || result
        ""           | TextUtil.splitBy(",").splitBy(":") || []
        "1:2,2:3"    | TextUtil.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
        "1:2, 2 : 3" | TextUtil.splitBy(",").splitBy(":") || [Pair.of("1", "2"), Pair.of("2", "3")]
    }


    def "Contains"() {
        expect:
        !TextUtil.contains(null, null)
        !TextUtil.contains("", null)
        !TextUtil.contains(null, "")
        TextUtil.contains("", "")
        TextUtil.contains("asd", "as")
        TextUtil.contains("Asd", "as")
        !TextUtil.contains("Asd", "as", false)
        TextUtil.contains("Asd", "aS")
        !TextUtil.contains("Asd", "aS", false)
        !TextUtil.contains("asd", "asb")
    }
}
