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

package com.es.lib.common.file

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 11.03.16
 */
class DeleteFileUtilSpec extends Specification {

    def "Silent delete null file expect false"() {
        expect:
        !DeleteFileUtil.silent(null)
    }

    def "Silent delete not exist file expect false"() {
        expect:
        !DeleteFileUtil.silent(new File('/tmp/undefined_file_from_spock_test.txt'))
    }

    def "Silent delete exist file expect true"() {
        when:
        def file = new File('/tmp/defined_file_from_spock_test.txt');
        then:
        file.createNewFile();
        expect:
        DeleteFileUtil.silent(file)
    }

    def "Exceptional delete null file expect exception"() {
        when:
        DeleteFileUtil.exceptional(null)
        then:
        thrown NullPointerException
    }

    def "Exceptional delete not exist file expect false"() {
        expect:
        !DeleteFileUtil.exceptional(new File('/tmp/undefined_file_from_spock_test.txt'))
    }

    def "Exceptional delete exist file expect true"() {
        when:
        def file = new File('/tmp/defined_file_from_spock_test.txt');
        then:
        file.createNewFile();
        expect:
        DeleteFileUtil.exceptional(file)
    }
}
