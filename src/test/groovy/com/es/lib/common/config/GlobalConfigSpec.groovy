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

package com.es.lib.common.config

import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.07.15
 */
class GlobalConfigSpec extends Specification {

    def "Инициализация всех значений"() {
        when:
        def props = """ name=App
                        version=1"""
        GlobalConfig.instance.init {
            return new ByteArrayInputStream(props.bytes)
        }
        then:
        GlobalConfig.instance.name.equals("App")
        GlobalConfig.instance.version.equals("1")
    }
}
