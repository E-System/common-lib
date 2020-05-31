package com.es.lib.common.security


import org.apache.commons.lang3.StringUtils
import spock.lang.Specification

class PasswordsSpec extends Specification {

    def "Numeric"() {
        when:
        def password = Passwords.numeric()
        then:
        password.length() == Passwords.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.NUMBERS_ALPHABET)
    }

    def "Numeric with length"() {
        when:
        def len = 8
        def password = Passwords.numeric(len)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.NUMBERS_ALPHABET)
    }

    def "AlphaNumeric"() {
        when:
        def password = Passwords.alphaNumeric()
        then:
        password.length() == Passwords.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.LOWERCASE_LETTERS_AND_NUMBERS_ALPHABET)
    }

    def "AlphaNumeric with caps"() {
        when:
        def password = Passwords.alphaNumeric(true)
        then:
        password.length() == Passwords.DEFAULT_LENGTH
        StringUtils.containsOnly(password, RandPass.NUMBERS_AND_LETTERS_ALPHABET)
    }

    def "AlphaNumeric with caps and length"() {
        when:
        def len = 10
        def password = Passwords.alphaNumeric(len, true)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.NUMBERS_AND_LETTERS_ALPHABET)
    }

    def "Generate"() {
        when:
        def len = 40
        def password = Passwords.generate(len, RandPass.PRINTABLE_ALPHABET)
        then:
        password.length() == len
        StringUtils.containsOnly(password, RandPass.PRINTABLE_ALPHABET)
    }

    def "CheckStrength all"() {
        when:
        def res = Passwords.checkStrength("", 10, true, true, true)
        then:
        res.size() == 4
        res.contains(Passwords.StrengthFeature.Blank)
        res.contains(Passwords.StrengthFeature.Length)
        res.contains(Passwords.StrengthFeature.Digit)
        res.contains(Passwords.StrengthFeature.Letter)
    }

    def "CheckStrength success"() {
        when:
        def res = Passwords.checkStrength("asd123zxct", 10, true, true, true)
        then:
        res.size() == 0
    }

    def "CheckStrength without digit"() {
        when:
        def res = Passwords.checkStrength("qwerasdfzxcv", 10, true, true, true)
        then:
        res.size() == 1
        res.contains(Passwords.StrengthFeature.Digit)
    }

    def "CheckStrength without letter"() {
        when:
        def res = Passwords.checkStrength("1234567890", 10, true, true, true)
        then:
        res.size() == 1
        res.contains(Passwords.StrengthFeature.Letter)
    }
}
