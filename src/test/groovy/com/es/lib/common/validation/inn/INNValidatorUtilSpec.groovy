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

package com.es.lib.common.validation.inn

import com.es.lib.common.validation.BadLengthException
import com.es.lib.common.validation.BadValueException
import spock.lang.Specification

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.05.15
 */
class INNValidatorUtilSpec extends Specification {

    def "Исключение для значений длина которых не 10 и не 12"() {
        when:
        INNValidatorUtil.validate(value)
        then:
        thrown(BadLengthException)
        where:
        value << ["123"]
    }

    def "При передаче null не должна возникать проверка"() {
        when:
        INNValidatorUtil.validate(value)
        then:
        true
        where:
        value << [null as String]
    }

    def "Для некорректных ИНН должны выкидывать исключения"() {
        when: "Происходит валидация"
        INNValidatorUtil.validate(value)
        then: "Кидаем исключение"
        thrown(BadValueException)
        where:
        value << ["1234567890", "1111111111", "123456789012", "111111111111"]
    }

    def "Корректные ИНН: 12 символов: 500100732259, 10 символов: 7830002293"() {
        when:
        INNValidatorUtil.validate(value)
        then:
        true
        where:
        value << ["500100732259", "7830002293"]
    }
}
