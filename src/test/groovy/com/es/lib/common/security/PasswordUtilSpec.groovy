package com.es.lib.common.security

import com.es.lib.common.security.PasswordUtil
import com.es.lib.common.security.RandPass
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

class PasswordUtilSpec extends Specification {

    def "Numeric"() {
        when:
        def password = PasswordUtil.numeric()
        then:
        password.length() == PasswordUtil.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.NUMBERS_ALPHABET)
    }

    def "Numeric with length"() {
        when:
        def len = 8
        def password = PasswordUtil.numeric(len)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.NUMBERS_ALPHABET)
    }

    def "AlphaNumeric"() {
        when:
        def password = PasswordUtil.alphaNumeric()
        then:
        password.length() == PasswordUtil.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.LOWERCASE_LETTERS_AND_NUMBERS_ALPHABET)
    }

    def "AlphaNumeric with caps"() {
        when:
        def password = PasswordUtil.alphaNumeric(true)
        then:
        password.length() == PasswordUtil.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.NUMBERS_AND_LETTERS_ALPHABET)
    }

    def "AlphaNumeric with caps and length"() {
        when:
        def len = 10
        def password = PasswordUtil.alphaNumeric(len, true)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.NUMBERS_AND_LETTERS_ALPHABET)
    }

    def "Generate"() {
        when:
        def len = 40
        def password = PasswordUtil.generate(len, RandPass.PRINTABLE_ALPHABET)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.PRINTABLE_ALPHABET)
    }

    def "CheckStrength all"() {
        when:
        def res = PasswordUtil.checkStrength("", 10, true, true, true)
        then:
        res.size() == 4
        res.contains(PasswordUtil.StrengthFeature.Blank)
        res.contains(PasswordUtil.StrengthFeature.Length)
        res.contains(PasswordUtil.StrengthFeature.Digit)
        res.contains(PasswordUtil.StrengthFeature.Letter)
    }

    def "CheckStrength success"() {
        when:
        def res = PasswordUtil.checkStrength("asd123zxct", 10, true, true, true)
        then:
        res.size() == 0
    }

    def "CheckStrength without digit"() {
        when:
        def res = PasswordUtil.checkStrength("qwerasdfzxcv", 10, true, true, true)
        then:
        res.size() == 1
        res.contains(PasswordUtil.StrengthFeature.Digit)
    }

    def "CheckStrength without letter"() {
        when:
        def res = PasswordUtil.checkStrength("1234567890", 10, true, true, true)
        then:
        res.size() == 1
        res.contains(PasswordUtil.StrengthFeature.Letter)
    }
}
