package com.eslibs.common.security

import com.eslibs.common.collection.Items
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
        def res = Passwords.strength("", new Passwords.LengthFeature(10), new Passwords.BlankFeature(), new Passwords.DigitFeature(), new Passwords.LetterFeature())
        then:
        res.size() == 4
        Items.firstByClass(res, Passwords.BlankFeature).isPresent()
        Items.firstByClass(res, Passwords.LengthFeature).isPresent()
        Items.firstByClass(res, Passwords.DigitFeature).isPresent()
        Items.firstByClass(res, Passwords.LengthFeature).isPresent()
    }

    def "CheckStrength success"() {
        when:
        def res = Passwords.strength("asd123zxct", new Passwords.LengthFeature(10), new Passwords.BlankFeature(), new Passwords.DigitFeature(), new Passwords.LetterFeature())
        then:
        res.size() == 0
    }

    def "CheckStrength without digit"() {
        when:
        def res = Passwords.strength("qwerasdfzxcv", new Passwords.LengthFeature(10), new Passwords.BlankFeature(), new Passwords.DigitFeature(), new Passwords.LetterFeature())
        then:
        res.size() == 1
        Items.firstByClass(res, Passwords.DigitFeature).isPresent()
    }

    def "CheckStrength without letter"() {
        when:
        def res = Passwords.strength("1234567890", new Passwords.LengthFeature(10), new Passwords.BlankFeature(), new Passwords.DigitFeature(), new Passwords.LetterFeature())
        then:
        res.size() == 1
        Items.firstByClass(res, Passwords.LetterFeature).isPresent()
    }
}
