package com.es.lib.common

import spock.lang.Specification

import java.security.SecureRandom

class RandPassSpec extends Specification {

    def "Generate password with default settings"() {
        given:
        RandPass randPass = new RandPass()

        when:
        String passDefault = randPass.getPass()
        String passLen = randPass.getPass(12)

        then:
        passDefault.length() == 8
        passLen.length() == 12
    }

    def "Generate password with custom alphabets and requirements"() {
        given:
        RandPass randPass = new RandPass(new SecureRandom(), RandPass.NUMBERS_AND_LETTERS_ALPHABET)
        randPass.setFirstAlphabet(RandPass.UPPERCASE_LETTERS_ALPHABET)
        randPass.setLastAlphabet(RandPass.NUMBERS_ALPHABET)
        randPass.addRequirement(RandPass.SYMBOLS_ALPHABET, 2)
        randPass.setAlphabet(RandPass.PRINTABLE_ALPHABET)
        randPass.setMaxRepetition(1)

        when:
        String pass = randPass.getPass(16)

        then:
        pass.length() == 16
        pass.charAt(0) in RandPass.UPPERCASE_LETTERS_ALPHABET
        pass.charAt(15) in RandPass.NUMBERS_ALPHABET

        when:
        RandPass simplePass = new RandPass(RandPass.LOWERCASE_LETTERS_ALPHABET)
        String passSimple = simplePass.getPass(6)

        then:
        passSimple.length() == 6
        passSimple.chars().allMatch { ch -> Character.isLowerCase((char) ch) }
    }
}
